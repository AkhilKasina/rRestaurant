package com.softwarestudiogroup1.uts.eRestaurant.controllers.customer;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingItemRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.ItemRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Booking;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.BookingItem;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class CustomerController {

    private int currentID = 0;

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final BookingItemRepository bookingItemRepository;

    public CustomerController(CustomerRepository customerRepository, BookingRepository bookingRepository, ItemRepository itemRepository, BookingItemRepository bookingItemRepository) {
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.bookingItemRepository = bookingItemRepository;
    }

    /**
    * This function is used for a purpose of database testing. 
    * Has to remove when publishing website
    */
    @GetMapping("/allcustomers")
    public String getHomePage(Model model) {
        List<Customer> cusLists = customerRepository.findAll();

        model.addAttribute("customerLists", cusLists);

        return "customers/allcustomers";
        
    }

    @GetMapping("/booking")
    public String getBookingPage(@ModelAttribute("customerID") int customerID, Model model) {
        
        this.currentID = customerID;
        Optional<Customer> currentCus = customerRepository.findById(customerID);

        if (currentCus.isPresent()) {
             Customer customer = currentCus.get();
             model.addAttribute("customer", customer);

        }

        else {
            return "redirect:/";
        }

        return ViewManager.CUS_PORTAL;
    }

    // CREATE BOOKING

    @GetMapping(value="/booking/new")
    public String processBookingForm(Model model) {
        BookingDAO bookingDAO = new BookingDAO();
        
        ArrayList<BookingItemDAO> bookingItemDAOs = new ArrayList<>();
        List<Item> itemLists = itemRepository.findAll();

        //only puts lunch items into lunchList
        for(Item item : itemLists){
            if(item.getMenuType().equals("lunch")){
                BookingItemDAO bookingItemDAO = new BookingItemDAO();

                bookingItemDAO.setItemID(item.getId());
                bookingItemDAO.setName(item.getName());
                bookingItemDAO.setPrice(item.getPrice());
                bookingItemDAO.setDescription(item.getDescription());
                bookingItemDAO.setQuantity("0");

                bookingItemDAOs.add(bookingItemDAO);
            }
        }

        bookingDAO.setBookingItems(bookingItemDAOs);

        model.addAttribute("customerBooking", bookingDAO);
        return ViewManager.CUS_BOOKING;
    }
    
    @PostMapping("/booking/new")
    public String processBooking(@ModelAttribute("customerBooking") BookingDAO bookingDAO, final RedirectAttributes redirectAttributes) {

        // Validate Date and Time here
        if ( bookingDAO.getBookingDate().replaceAll("\\s+","").isEmpty() == false
        && bookingDAO.getBookingTime().replaceAll("\\s+","").isEmpty() == false) {
            Optional<Customer> currentCus = customerRepository.findById(currentID);

            System.out.println("Booking: " + bookingDAO.getBookingDate() + " time: " + bookingDAO.getBookingTime() + " table: " + bookingDAO.getTablePosition());

            if (currentCus.isPresent()) {
                Customer customer = currentCus.get();
                
                // Save Booking To Database
                Booking newBooking = new Booking();
                newBooking.setBookingDateTime(bookingDAO.getBookingTimeStamp());
                newBooking.setBookingTime(bookingDAO.getBookingTime());
                newBooking.setBookingDate(bookingDAO.getBookingDate());
                newBooking.setTablePosition(bookingDAO.getTablePosition());
                newBooking.setCustomer(customer);

                bookingRepository.save(newBooking);

                saveBookingItemsToDBs(bookingDAO.getBookingItems(), newBooking);
            
            }
        }
        
        return redirectToCustomerPortal(redirectAttributes);
    }

    // EDIT BOOKING
    @GetMapping("/booking/{bookingID}")
    public String editBooking(@PathVariable("bookingID") int bookingID, Model model) {
        Booking currentBooking = this.bookingRepository.findById(bookingID).get();
        List<BookingItem> currentBookItems = currentBooking.getBookingItems();

        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.setId(currentBooking.getId());
        bookingDAO.setDateAndTime(currentBooking.getBookingDateTime());
        bookingDAO.setTablePosition(currentBooking.getTablePosition());
        

        ArrayList<BookingItemDAO> bookingItemsDAO = new ArrayList<>();
        List<Item> itemLists = itemRepository.findAll();

        //only puts lunch items into lunchList
        for(Item item : itemLists){
            if(item.getMenuType().equals("lunch")){
                BookingItemDAO bookingItemDAO = new BookingItemDAO();

                bookingItemDAO.setItemID(item.getId());
                bookingItemDAO.setName(item.getName());
                bookingItemDAO.setPrice(item.getPrice());
                bookingItemDAO.setDescription(item.getDescription());
                bookingItemDAO.setQuantity("0");

                for (BookingItem bookingItem: currentBookItems) {
                    if (bookingItem.getItem().getId() == item.getId()) {
                        bookingItemDAO.setQuantity("" + bookingItem.getQuantity());
                    }
                }

                bookingItemsDAO.add(bookingItemDAO);
            }
        }

        bookingDAO.setBookingItems(bookingItemsDAO);

        model.addAttribute("allowDelete", true);
        model.addAttribute("customerBooking", bookingDAO);
        return ViewManager.CUS_BOOKING;
    }

    @PostMapping("/booking/{bookingID}")
    public String updateBooking(@ModelAttribute("customerBooking") BookingDAO bookingDAO, final RedirectAttributes redirectAttributes) {
        Optional<Customer> currentCus = customerRepository.findById(currentID);
        Optional<Booking> currentBooking = this.bookingRepository.findById(bookingDAO.getId());

        if (currentCus.isPresent() &&  currentBooking.isPresent()) {
            Booking updatedBooking = currentBooking.get();
            updatedBooking.setBookingDateTime(bookingDAO.getBookingTimeStamp());
            updatedBooking.setBookingTime(bookingDAO.getBookingTime());
            updatedBooking.setBookingDate(bookingDAO.getBookingDate());
            updatedBooking.setTablePosition(bookingDAO.getTablePosition());
            updatedBooking.setCustomer(currentCus.get());

            saveBookingItemsToDBs(bookingDAO.getBookingItems(), updatedBooking);

            this.bookingRepository.save(currentBooking.get());
        }

        return redirectToCustomerPortal(redirectAttributes);
    }

    // DELETE BOOKING
    @GetMapping("/booking/{bookingID}/delete")
    public String deleteBooking(@PathVariable("bookingID") int bookingID, final RedirectAttributes redirectAttributes) {

        if ( bookingRepository.existsById(bookingID)) {
            this.bookingRepository.deleteById(bookingID);
        }

        return redirectToCustomerPortal(redirectAttributes);
    }

    // CONVENIENCE

    /**
     * Transfering bookingItemDAOs to BookingItem Entities. 
     * Then saved into database with corresponding Booking Entity
     * 
     * @param bookingItemDAOs: List of DAOs
     * @param savedBooking: Booking that contain items
     */
    private void saveBookingItemsToDBs(List<BookingItemDAO> bookingItemDAOs, Booking savedBooking) {
        ArrayList<BookingItem> newBookingItems = new ArrayList<>();

        for (BookingItemDAO bookItemIter: bookingItemDAOs) {
            BookingItem newBookingItem = new BookingItem();

            Optional<Item> item = itemRepository.findById(bookItemIter.getItemID());
            int quantity = Integer.parseInt(bookItemIter.getQuantity());
                    
            // Validate item to save into database
            if (item.isPresent() && quantity >= 0 ) {
                newBookingItem.setBooking(savedBooking);
                newBookingItem.setItem(item.get());
                newBookingItem.setQuantity(quantity);

                newBookingItems.add(newBookingItem);

                this.bookingItemRepository.save(newBookingItem);

            }
                    
         }

    }

    private String redirectToCustomerPortal(final RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("customerID", this.currentID);
        return "redirect:/booking";
    }

    //REWARDS PAGE

    @GetMapping(value="/viewRewards")
    public String getRewardsPage(Model model) {
        Optional<Customer> currentCus = customerRepository.findById(this.currentID);

        if (currentCus.isPresent()) {
            Customer customer = currentCus.get();
            model.addAttribute("customer", customer);

       }

        return ViewManager.CUS_REWARDS;
    }
}


