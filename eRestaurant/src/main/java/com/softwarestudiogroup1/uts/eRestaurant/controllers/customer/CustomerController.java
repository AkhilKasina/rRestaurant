package com.softwarestudiogroup1.uts.eRestaurant.controllers.customer;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.controllers.manager.StaffDAO;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingItemRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.ItemRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.RewardRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Booking;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.BookingItem;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Item;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Reward;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class CustomerController {

    private int currentID = 0;
    public void setID(int id) {
        this.currentID = id;
    }

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final BookingItemRepository bookingItemRepository;
    private final RewardRepository rewardRepository;

    public CustomerController(CustomerRepository customerRepository, BookingRepository bookingRepository, ItemRepository itemRepository, BookingItemRepository bookingItemRepository, RewardRepository rewardRepository) {
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.bookingItemRepository = bookingItemRepository;
        this.rewardRepository = rewardRepository;
    }

    @GetMapping("/booking")
    public String getBookingPage(Model model) {

        Optional<Customer> currentCus = customerRepository.findById(this.currentID);
        if (currentCus.isPresent()) {
            Customer customer = currentCus.get();
            model.addAttribute("customer", customer);

            //expiring reward
            Set<Reward> rewards = customer.getRewards();
            Reward currentReward = rewards.iterator().next();
            for(Reward r : rewards){
                int compare = (r.getExpiryDate()).compareTo(currentReward.getExpiryDate());
                if(compare < 0){
                    currentReward = r;
                }
            }
            model.addAttribute("expreward", currentReward);

        }
        else {
            return "redirect:/";
        }

        

        

        return ViewManager.CUS_PORTAL;
    }

    // CREATE BOOKING

    @GetMapping(value="/booking/new")
    public String processBookingForm(Model model) {

        Optional<Customer> currentCus = customerRepository.findById(currentID);
        
        if (currentCus.isEmpty()) {
            return "redirect:/";
        }

        BookingDAO bookingDAO = new BookingDAO();

        model.addAttribute("customerBooking", bookingDAO);
        model.addAttribute("isEditing", false);
        return ViewManager.CUS_BOOKING;
    }
    
    @PostMapping("/booking/new")
    public String processBooking(@ModelAttribute("customerBooking") BookingDAO bookingDAO, final RedirectAttributes redirectAttributes, Model model) {

        Optional<Customer> currentCus = customerRepository.findById(currentID);
        
        if (currentCus.isEmpty()) {
            return "redirect:/";
        }

        // Validate Date and Time here
        if ( !bookingDAO.getBookingDate().replaceAll("\\s+","").isEmpty()
        && !bookingDAO.getBookingTime().replaceAll("\\s+","").isEmpty()) {
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

                if (bookingDAO.getBookingItems() != null) {
                    saveBookingItemsToDBs(bookingDAO.getBookingItems(), newBooking);
                }
            
            }

            // Successful Book
            return redirectToCustomerPortal(redirectAttributes);
        } 
        else if (bookingDAO.getBookingDate().replaceAll("\\s+","").isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage","Please Enter Booking Date!");

        } 
        else if (bookingDAO.getBookingTime().replaceAll("\\s+","").isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage","Please Enter Booking Time!");
        }
        else if (!isTimeWithin( BookingType.BOTH ,bookingDAO.getBookingTime())) {
            model.addAttribute("timeError", true);
            model.addAttribute("timeErrorMessage", "Lunch 12PM-4PM | Dinner 4PM-9PM"); 
        }

        model.addAttribute("isEditing", false);
        return ViewManager.CUS_BOOKING;
    }

    @RequestMapping(value = "/booking/new", method = RequestMethod.POST, params = "cancel")
    public String cancelBooking(@ModelAttribute("customerBooking") BookingDAO bookingDAO, 
        final Model model, final RedirectAttributes redirectAttributes) {
        return redirectToCustomerPortal(redirectAttributes);
    }
    
    @RequestMapping(value = "/booking/new", method = RequestMethod.POST, params = "showMenu")
    public String showMenu(@ModelAttribute("customerBooking") BookingDAO bookingDAO, 
        final Model model, final RedirectAttributes redirectAttributes) {
        
        System.out.println("isTimeWithinBoth " + bookingDAO.getBookingTime());

        if (bookingDAO.getBookingDate().replaceAll("\\s+","").isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage","Please Enter Booking Date!");

        } 
        else if (bookingDAO.getBookingTime().replaceAll("\\s+","").isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage","Please Enter Booking Time!");
        }
        else if (!isTimeWithin(BookingType.BOTH ,bookingDAO.getBookingTime())) {
            
            model.addAttribute("timeError", true);
            model.addAttribute("timeErrorMessage", "Lunch 12PM-4PM | Dinner 4PM-9PM"); 
        }
        else  {
            List<Item> itemsList = itemRepository.findAll();

            if (isTimeWithin(BookingType.LUNCH ,bookingDAO.getBookingTime())) {
                bookingDAO.setBookingItemsFrom(itemsList, BookingType.LUNCH);

            } else if (isTimeWithin(BookingType.DINNER, bookingDAO.getBookingTime())) {
                bookingDAO.setBookingItemsFrom(itemsList, BookingType.DINNER);
            }

        }
        model.addAttribute("customerBooking", bookingDAO);
        model.addAttribute("isEditing", false);
        
        return ViewManager.CUS_BOOKING;
    }

    // EDIT BOOKING
    @GetMapping("/booking/{bookingID}")
    public String editBooking(@PathVariable("bookingID") int bookingID, Model model) {

        Optional<Customer> currentCus = customerRepository.findById(currentID);
        
        if (currentCus.isEmpty()) {
            return "redirect:/";
        }

        Booking currentBooking = this.bookingRepository.findById(bookingID).get();
        List<BookingItem> currentBookItems = currentBooking.getBookingItems();

        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.setId(currentBooking.getId());
        // bookingDAO.setDateAndTime(currentBooking.getBookingDateTime());
        bookingDAO.setBookingDate(currentBooking.getBookingDate());
        bookingDAO.setBookingTime(currentBooking.getBookingTime());
        bookingDAO.setTablePosition(currentBooking.getTablePosition());
        

        ArrayList<BookingItemDAO> bookingItemsDAO = new ArrayList<>();
        List<Item> itemLists = itemRepository.findAll();

        BookingType currentBookingType = bookingType(currentBooking.getBookingTime());
        if (currentBookingType == BookingType.LUNCH) {
            bookingDAO.setBookingItemQuantity(currentBookItems, BookingType.LUNCH, itemLists);
        } else {
            bookingDAO.setBookingItemQuantity(currentBookItems, BookingType.DINNER, itemLists);
        }

        model.addAttribute("allowDelete", true);
        model.addAttribute("isEditing", true);
        model.addAttribute("customerBooking", bookingDAO);
        return ViewManager.CUS_BOOKING;
    }

    @RequestMapping(value = "/booking/{bookingID}", method = RequestMethod.POST, params = "cancel")
    public String cancelBooking(@PathVariable("bookingID") int bookingID, 
        final Model model, final RedirectAttributes redirectAttributes) {
        return redirectToCustomerPortal(redirectAttributes);
    }

    @PostMapping("/booking/{bookingID}")
    public String updateBooking(@ModelAttribute("customerBooking") BookingDAO bookingDAO, final RedirectAttributes redirectAttributes, Model model) {
        Optional<Customer> currentCus = customerRepository.findById(currentID);
        Optional<Booking> currentBooking = this.bookingRepository.findById(bookingDAO.getId());

        if (currentCus.isPresent() &&  currentBooking.isPresent()) {
            Booking updatedBooking = currentBooking.get();
            List<BookingItem> currentBookItems = updatedBooking.getBookingItems();

            List<Item> itemLists = itemRepository.findAll();

            BookingType currentBookingType = bookingType(updatedBooking.getBookingTime());
            BookingType customerInputBookingType = bookingType(bookingDAO.getBookingTime());

            if (currentBookingType != customerInputBookingType) {
                System.out.println("Different Time");
                
                model.addAttribute("error", true);
                if (currentBookingType == BookingType.LUNCH) {
                    model.addAttribute("errorMessage", "Please book within lunch time | 12PM - 4PM");
                } else {
                    model.addAttribute("errorMessage", "Please book within dinner time | 4PM - 9PM");
                }

                if (currentBookingType == BookingType.LUNCH) {
                    bookingDAO.setBookingItemQuantity(currentBookItems, BookingType.LUNCH, itemLists);
                } else {
                    bookingDAO.setBookingItemQuantity(currentBookItems, BookingType.DINNER, itemLists);
                }

                model.addAttribute("allowDelete", true);
                model.addAttribute("isEditing", true);
                model.addAttribute("customerBooking", bookingDAO);
                return ViewManager.CUS_BOOKING;
            }

           

            updatedBooking.setBookingDateTime(bookingDAO.getBookingTimeStamp());
            updatedBooking.setBookingTime(bookingDAO.getBookingTime());
            updatedBooking.setBookingDate(bookingDAO.getBookingDate());
            updatedBooking.setTablePosition(bookingDAO.getTablePosition());
            updatedBooking.setCustomer(currentCus.get());

            System.out.println("Update Booking: " + updatedBooking.getBookingDateTime()  +" table: " + bookingDAO.getTablePosition());

            saveBookingItemsToDBs(bookingDAO.getBookingItems(), updatedBooking);

            this.bookingRepository.save(currentBooking.get());
        }

        return redirectToCustomerPortal(redirectAttributes);
    }

    // DELETE BOOKING
    @GetMapping("/booking/{bookingID}/delete")
    public String deleteBooking(@PathVariable("bookingID") int bookingID, final RedirectAttributes redirectAttributes) {

        Optional<Customer> currentCus = customerRepository.findById(currentID);
        
        if (currentCus.isEmpty()) {
            return "redirect:/";
        }

        if ( bookingRepository.existsById(bookingID)) {
            this.bookingRepository.deleteById(bookingID);
        }

        return redirectToCustomerPortal(redirectAttributes);
    }

    // LOG OUT
    @RequestMapping(value = {"/booking"}, method = RequestMethod.POST, params = "logout")
    public String logout() {
        this.currentID = -1;
        return "redirect:/";
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

    private BookingType bookingType(String timeString) {
        if (isTimeWithin(BookingType.LUNCH, timeString)) {
            return BookingType.LUNCH;
        } else if (isTimeWithin(BookingType.DINNER, timeString)) {
            return BookingType.DINNER;
        }

        return BookingType.BOTH;
    }

    private Boolean isTimeWithin(BookingType type, String timeString) {
        try {
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date time = timeFormat.parse(timeString);

            Date openLunchTime = timeFormat.parse("12:00");
            Date endLunchTime = timeFormat.parse("15:59");

            Date openDinnerTime = timeFormat.parse("16:00");
            Date endDinnerTime = timeFormat.parse("21:00");
          
            if (type.equals(BookingType.BOTH)) {
                return time.after(openLunchTime) && time.before(endDinnerTime);
            }
            else if (type.equals(BookingType.LUNCH)) {
                return time.after(openLunchTime) && time.before(endLunchTime);
            } 
            else if (type.equals(BookingType.DINNER)) {
                return time.after(openDinnerTime) && time.before(endDinnerTime);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

       return false;
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

       } else  {
           return "redirect:/";
       }

        return ViewManager.CUS_REWARDS;
    }

    //helper function to exchange reward
    public String exchangeReward(String name, double discount, int point, Model model) {
        Optional<Customer> currentCus = customerRepository.findById(this.currentID);

        if (currentCus.isPresent()) {
            Customer customer = currentCus.get();
            boolean check = (customer.getPoints() >= point);
            if(check){
                String currentDate = java.time.LocalDate.now().toString();
                String expDate = java.time.LocalDate.now().plusDays(30).toString();
                Reward reward = new Reward(name, discount, currentDate , expDate, customer);
                this.rewardRepository.save(reward);
                customer.setPoints(customer.getPoints() - point);
                customerRepository.save(customer);
            } else{
                model.addAttribute("check", check);
            }
       } 
       else {
            return "redirect:/";
       }

       return "redirect:/viewRewards";
    }

    //exchanges 100 points for 10%
    @PostMapping("/exchange10")
    public String exchangeTen(Model model) {
       return exchangeReward("10OFF", 10, 100, model);
    }

    //exchanges 180 points for 15%
    @PostMapping("/exchange15")
    public String exchangeFifteen(Model model) {
        return exchangeReward("15OFF", 15, 250, model);
    }

    //exchanges 250 points for 20%
    @PostMapping("/exchange20")
    public String exchangeTwenty(Model model) {
        return exchangeReward("20OFF", 20, 250, model);
    }

    @GetMapping("/return")
    public String exitRewards(final RedirectAttributes redirectAttributes) {
        return redirectToCustomerPortal(redirectAttributes);
    }

    
}


