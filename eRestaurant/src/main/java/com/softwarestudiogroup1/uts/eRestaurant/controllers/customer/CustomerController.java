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

    String dateError = "errorMessage";
    String isDateError = "error";

    String timeError = "timeErrorMessage";
    String isTimeError = "timeError";

    @GetMapping("/booking")
    public String getBookingPage(Model model) {

        if (!customerRepository.existsById(currentID) ) {
            return "redirect:/";
        }

        Customer currentCus = customerRepository.findById(this.currentID).get();
        model.addAttribute("customer", currentCus);

        //expiring reward
        List<Reward> rewards = currentCus.getRewards();
        if(rewards.isEmpty()){
            Reward noreward = new Reward();
            noreward.setId(1);
            noreward.setRewardName("NONE");
            noreward.setDiscount(0);
            noreward.setExpiryDate("2077-01-01");
            noreward.setDateAcquired(java.time.LocalDate.now().toString());
            noreward.setCustomer(currentCus);
            currentCus.setPoints(200);
            this.rewardRepository.save(noreward);
            this.customerRepository.save(currentCus);
            model.addAttribute("expreward", noreward);
        }else{
            Reward currentReward = rewards.iterator().next();
            for(Reward r : rewards){
                int compare = (r.getExpiryDate()).compareTo(currentReward.getExpiryDate());
                if(compare < 0){
                    currentReward = r;
                }
            }
            model.addAttribute("expreward", currentReward);
        }

        return ViewManager.CUS_PORTAL;
    }

    // CREATE BOOKING

    @GetMapping(value="/booking/new")
    public String processBookingForm(Model model) {

        if (!customerRepository.existsById(currentID) ) {
            return "redirect:/";
        }
        Customer currentCus = customerRepository.findById(this.currentID).get();
        model.addAttribute("customer", currentCus);

        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.setCustomer(currentCus);
        
        model.addAttribute("bookingType", "");
        model.addAttribute("customerBooking", bookingDAO);
        model.addAttribute("isEditing", false);
        return ViewManager.CUS_BOOKING;
    }
    
    @PostMapping("/booking/new")
    public String processBooking(@ModelAttribute("customerBooking") BookingDAO bookingDAO, final RedirectAttributes redirectAttributes, Model model) {

        if (!customerRepository.existsById(currentID) ) { return "redirect:/"; }

        Customer customer = customerRepository.findById(this.currentID).get();
        bookingDAO.setCustomer(customer);

        // No Errors in Validation
        if (bookingValidationToModel(model, bookingDAO) == null) {
            System.out.println("Booking: " + bookingDAO.getBookingDate() + " time: " + bookingDAO.getBookingTime() + " table: " + bookingDAO.getTablePosition());
                
            // Save Booking To Database
            Booking newBooking = new Booking();
            newBooking.setBookingDateTime(bookingDAO.getBookingTimeStamp());
            newBooking.setBookingTime(bookingDAO.getBookingTime());
            newBooking.setBookingDate(bookingDAO.getBookingDate());
            newBooking.setTablePosition(bookingDAO.getTablePosition());
            newBooking.setCustomer(customer);
            newBooking.setReward(bookingDAO.getReward());

            bookingRepository.save(newBooking);

            if (bookingDAO.getBookingItems() != null) {
                saveBookingItemsToDBs(bookingDAO.getBookingItems(), newBooking);
            }

            return redirectToCustomerPortal(redirectAttributes);
        } 
        // Errors Found
        else {
            model = bookingValidationToModel(model, bookingDAO);

            model.addAttribute("isEditing", false);
            return ViewManager.CUS_BOOKING;
        }
    }

    @RequestMapping(value = "/booking/new", method = RequestMethod.POST, params = "cancel")
    public String cancelBooking(@ModelAttribute("customerBooking") BookingDAO bookingDAO, 
        final Model model, final RedirectAttributes redirectAttributes) {
        return redirectToCustomerPortal(redirectAttributes);
    }
    
    @RequestMapping(value = "/booking/new", method = RequestMethod.POST, params = "showMenu")
    public String showMenu(@ModelAttribute("customerBooking") BookingDAO bookingDAO, 
        Model model, final RedirectAttributes redirectAttributes) {
        Customer currentCus = customerRepository.findById(this.currentID).get();
        model.addAttribute("customer", currentCus);
        bookingDAO.setCustomer(currentCus);
        
        System.out.println("isTimeWithinBoth " + bookingDAO.getBookingTime());

        model.addAttribute("bookingType", "");

        // No Error Found
        if (bookingValidationToModel(model, bookingDAO) == null) {
            List<Item> itemsList = itemRepository.findAll();

            if (isTimeWithin(BookingType.LUNCH ,bookingDAO.getBookingTime())) {
                model.addAttribute("bookingType", "Lunch" );
                bookingDAO.setBookingItemsFrom(itemsList, BookingType.LUNCH);

            } else if (isTimeWithin(BookingType.DINNER, bookingDAO.getBookingTime())) {
                model.addAttribute("bookingType", "Dinner");
                bookingDAO.setBookingItemsFrom(itemsList, BookingType.DINNER);
            }
        }
        // Error Found
        else {
            model = bookingValidationToModel(model, bookingDAO);
        }

        model.addAttribute("customerBooking", bookingDAO);
        model.addAttribute("isEditing", false);
        
        
        return ViewManager.CUS_BOOKING;
    }

    // EDIT BOOKING
    @GetMapping("/booking/{bookingID}")
    public String editBooking(@PathVariable("bookingID") int bookingID, Model model) {

        if (!customerRepository.existsById(currentID)) {
            return "redirect:/";
        }
        Customer customer = customerRepository.findById(this.currentID).get();
        model.addAttribute("customer", customer);


        Booking currentBooking = this.bookingRepository.findById(bookingID).get();
        List<BookingItem> currentBookItems = currentBooking.getBookingItems();

        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.setCustomer(customer);
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
            model.addAttribute("bookingType", "Lunch" );
        } else {
            bookingDAO.setBookingItemQuantity(currentBookItems, BookingType.DINNER, itemLists);
            model.addAttribute("bookingType", "Dinner" );
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

        Booking updatedBooking = bookingRepository.existsById(bookingDAO.getId()) ? bookingRepository.findById(bookingDAO.getId()).get() : null;
        Customer currentCus = customerRepository.existsById(currentID) ? customerRepository.findById(currentID).get() : null;

        if (updatedBooking == null && currentCus == null) {
            redirectToCustomerPortal(redirectAttributes);
        }

        List<BookingItem> currentBookItems = updatedBooking.getBookingItems();

        BookingType currentBookingType = bookingType(updatedBooking.getBookingTime());
        BookingType customerInputBookingType = bookingType(bookingDAO.getBookingTime());

        
        if (currentBookingType != customerInputBookingType) {
            model.addAttribute(isTimeError, true);

            // If customer select dinner time for lunch booking, there will be error
            if (currentBookingType == BookingType.LUNCH) {
                model.addAttribute(timeError, "Please book within lunch time | 12PM - 4PM");
                bookingDAO.setBookingItemQuantity(currentBookItems, BookingType.LUNCH, itemRepository.findAll());
            } else {
                model.addAttribute(timeError, "Please book within dinner time | 4PM - 9PM");
                bookingDAO.setBookingItemQuantity(currentBookItems, BookingType.DINNER, itemRepository.findAll());
            }

            bookingDAO.setCustomer(currentCus);

            model.addAttribute("allowDelete", true);
            model.addAttribute("isEditing", true);
            model.addAttribute("customerBooking", bookingDAO);
            return ViewManager.CUS_BOOKING;
        }

        updatedBooking.setBookingDateTime(bookingDAO.getBookingTimeStamp());
        updatedBooking.setBookingTime(bookingDAO.getBookingTime());
        updatedBooking.setBookingDate(bookingDAO.getBookingDate());
        updatedBooking.setTablePosition(bookingDAO.getTablePosition());
        updatedBooking.setCustomer(currentCus);

        bookingItemRepository.deleteAll(updatedBooking.getBookingItems());
        updatedBooking.removeAllItems();

        saveBookingItemsToDBs(bookingDAO.getBookingItems(), updatedBooking);

        this.bookingRepository.save(updatedBooking);

        return redirectToCustomerPortal(redirectAttributes);
    }

    // DELETE BOOKING
    @RequestMapping(value = {"/booking/{bookingID}"}, method = RequestMethod.POST, params = "delete") 
    public String deleteCustomerBooking(@PathVariable("bookingID") int bookingID, Model model) {
        if (!customerRepository.existsById(currentID)) {
            return "redirect:/";
        }

        if (bookingRepository.existsById(bookingID)) {
            this.bookingRepository.deleteById(bookingID);
        }

        return "redirect:/booking";
    }

    // LOG OUT
    @RequestMapping(value = {"/booking"}, method = RequestMethod.POST, params = "logout")
    public String logout() {
        this.currentID = -1;
        return "redirect:/";
    }

    //REWARDS PAGE

    @GetMapping(value="/viewRewards")
    public String getRewardsPage(Model model) {
      
        if (!customerRepository.existsById(currentID) ) {
            return "redirect:/";
        }
        Customer currentCus = customerRepository.findById(this.currentID).get();

        model.addAttribute("customer", currentCus);

        return ViewManager.CUS_REWARDS;
    }

    //helper function to exchange reward
    public String exchangeReward(String name, double discount, int point, Model model) {
      
        if (!customerRepository.existsById(currentID) ) {
            return "redirect:/";
        }

        Customer currentCus = customerRepository.findById(this.currentID).get();

        boolean check = (currentCus.getPoints() >= point);
        if (check) {
            String currentDate = java.time.LocalDate.now().toString();
            String expDate = java.time.LocalDate.now().plusDays(30).toString();
            Reward reward = new Reward(name, discount, currentDate , expDate, currentCus);

            this.rewardRepository.save(reward);
            currentCus.setPoints(currentCus.getPoints() - point);
            customerRepository.save(currentCus);

        } else {
            model.addAttribute("check", check);
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
        return exchangeReward("15OFF", 15, 180, model);
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



    // CONVENIENCE FUNCTIONS

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

    private Boolean isDateValid(String dateString) {
    try {
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date currentDate = java.sql.Date.valueOf( java.time.LocalDate.now());
        Date userDate = timeFormat.parse(dateString);

        return userDate.after(currentDate);
    }
    catch (Exception e) {
        System.out.println(e);
    }
    
    return false;
    }


    /**
     * Update Model Depending on Validation
     * @param model
     * @return a model if there is errors, null if no errors
     */
    private Model bookingValidationToModel(Model model, BookingDAO bookingDAO) {
        if (bookingDAO.getBookingDate().replaceAll("\\s+","").isEmpty()) {
            model.addAttribute(isDateError, true);
            model.addAttribute(dateError,"Please Enter Booking Date!");

        } 
        else if (!isDateValid(bookingDAO.getBookingDate())) {
            model.addAttribute(isDateError, true);
            model.addAttribute(dateError, "Please select a day after today!");
        } 
        else if (bookingDAO.getBookingTime().replaceAll("\\s+","").isEmpty()) {
            model.addAttribute(isTimeError, true);
            model.addAttribute(timeError,"Please Enter Booking Time!");
        }
        else if (!isTimeWithin(BookingType.BOTH ,bookingDAO.getBookingTime())) {
            
            model.addAttribute(isTimeError,true);
            model.addAttribute(timeError, "Lunch 12PM-4PM | Dinner 4PM-9PM"); 
        } 
        else {
            return null;
        }

        return model;
    }

    private String redirectToCustomerPortal(final RedirectAttributes redirectAttributes) {
        return "redirect:/booking";
    }

    
}


