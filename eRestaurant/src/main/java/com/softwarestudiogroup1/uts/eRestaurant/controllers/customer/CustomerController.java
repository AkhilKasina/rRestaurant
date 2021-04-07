package com.softwarestudiogroup1.uts.eRestaurant.controllers.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;
import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;

@Controller
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
      
        Optional<Customer> currentCus = customerRepository.findById(customerID);

        if (currentCus.isPresent()) {
             Customer customer = currentCus.get();
             model.addAttribute("customerName", customer.getFirstName());
        }

        else {
            return "redirect:/";
        }

        return "customers/bookingPage";
    }

}
