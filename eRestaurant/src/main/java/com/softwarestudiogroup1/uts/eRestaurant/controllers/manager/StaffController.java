package com.softwarestudiogroup1.uts.eRestaurant.controllers.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.BookingRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.ManagerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.StaffRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Booking;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Manager;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Staff;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class StaffController {

    private int currentID = 0;

    private final ManagerRepository managerRepository;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;

    public StaffController(ManagerRepository managerRepository, StaffRepository staffRepository, 
    CustomerRepository customerRepository, BookingRepository bookingRepository) {
        this.managerRepository = managerRepository;
        this.staffRepository = staffRepository;
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;

    }
    
    @GetMapping("/staff")
    public String staffPortal(@ModelAttribute("staffID") int staffID, Model model) {
        this.currentID = staffID;
        Optional<Staff> currentStaff = staffRepository.findById(this.currentID);

        if (currentStaff.isPresent()) {
            Staff staff = currentStaff.get();
            model.addAttribute("staff", staff);
        }

        List<Booking> allBookings = this.bookingRepository.findAllByOrderByBookingDateTimeAsc();

        List<RecentBookingDAO> recentBookingDAOs = new ArrayList<>();

        for (Booking booking: allBookings) {
            Optional<Customer> customer = this.customerRepository.findById(booking.getId());
            if (customer.isPresent()) {
                Customer bookedCustomer = customer.get();

                RecentBookingDAO recentBookingDAO = new RecentBookingDAO("" +bookedCustomer.getId(), bookedCustomer.getFirstName(), bookedCustomer.getLastName());
                recentBookingDAO.setDateTimeString("" + booking.getBookingDateTime());

                recentBookingDAOs.add(recentBookingDAO);
            }
           
        }

        model.addAttribute("recentBookings", recentBookingDAOs);

        return ViewManager.STAFF_PORTAL;
    }
    
}
