package com.softwarestudiogroup1.uts.eRestaurant.controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.ManagerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.StaffRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Booking;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ManagerController {

    private int currentID = 0;

    private final ManagerRepository managerRepository;
    private final StaffRepository staffRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;

    public ManagerController(ManagerRepository managerRepository, StaffRepository staffRepository, BookingRepository bookingRepository, CustomerRepository customerRepository) {
        this.managerRepository = managerRepository;
        this.staffRepository = staffRepository;
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
    }
    
    @GetMapping("/manager")
    public String managerPortal(@ModelAttribute("managerID") int managerID, Model model) {
        this.currentID = managerID;
        Optional<Manager> currentManager = managerRepository.findById(this.currentID);

        int i = 0;
        String[] names = new String[bookingRepository.findAll().size()];
        for (Booking booking : bookingRepository.findAll()){
            Customer customer = customerRepository.findById(booking.getId()).get();
            names[i] = customer.getFirstName() + " " + customer.getLastName();
            i += 1;
        }
        model.addAttribute("bookings", bookingRepository.findAll());
        model.addAttribute("names", names);
        if (currentManager.isPresent()) {
            Manager manager = currentManager.get();
            model.addAttribute("manager", manager);
        }
        return ViewManager.MNG_PORTAL;
    }

    private String redirectToCustomerPortal(final RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("managerID", this.currentID);
        return "redirect:/manager";
    }
    
}
