package com.softwarestudiogroup1.uts.eRestaurant.controllers.system;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Manager;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Patron;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Staff;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.ManagerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.StaffRepository;

import org.springframework.ui.Model;

@Controller
public class SignInController {

    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final StaffRepository staffRepository;

    public SignInController(CustomerRepository customerRepository, ManagerRepository managerRepository, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.managerRepository = managerRepository;
        this.staffRepository = staffRepository;
    }
    
    @GetMapping("/login")
    public String signInScreen(Model model) {

        model.addAttribute("loginPatron", new Patron());

        return ViewManager.LOG_IN;
    }

    /**
     * Validate Log-In for Customer, Manager & Staff -> Redirect to their portal once successful
     * 
     * @param patron User that inputs
     * @param model  Spring Model to Connect to HTML
     * @param redirectAttributes Direct Attribute to direct current to new Page
     * @return
     */

    @PostMapping("/login")
    public String processLogIn(@ModelAttribute("loginPatron") Patron patron, Model model, final RedirectAttributes redirectAttributes) {
        
        boolean success = false;
        int id = -1;

        String username = patron.getUsername();
        String password = patron.getPassword();

        System.out.println("Login " +username + "  " + password);

        if (username.startsWith("M_") || username.startsWith("m_")) {
            // Manager Login
            
            Optional<Manager> currentManager = managerRepository.findByUserNameAndPassword(username, password);
            System.out.println(currentManager.get().getId());
            if (currentManager.isPresent()) {
                redirectAttributes.addFlashAttribute("managerID", currentManager.get().getId());
                return "redirect:/manager";
            }
        } 
        else if (username.startsWith("S_") || username.startsWith("s_")) {
            // Staff Login
            Optional<Staff> currentStaff = staffRepository.findByUserNameAndLastName(username, password);

            if (currentStaff.isPresent()) {
                redirectAttributes.addFlashAttribute("staffID", currentStaff.get().getId());
                return "redirect:/staff";
            }
        }
        else {
            // Customer Login
            List<Customer> cusLists = customerRepository.findAll();

            for (Customer currentCus: cusLists) {
                if (currentCus.getUsername().equals(username) && currentCus.getPassword().equals(password)) {
                    success = true;
                    id = currentCus.getId();
                    break;
                }
            }

            if (success == true) {
                redirectAttributes.addFlashAttribute("customerID", id);
                return "redirect:/booking";
            }
        }
        

        // Display Error instead
        model.addAttribute("success", success);
        return ViewManager.LOG_IN;
    }

    @GetMapping("/signupPage")
    public String signUpPage(Model model) {
        model.addAttribute("signupCustomer", new NewCustomerDAO());
        return ViewManager.SIGN_UP;
    }

    @PostMapping("/signupPage")
    public String customerSignUp(@ModelAttribute("signupCustomer") NewCustomerDAO newCustomerDAO, final RedirectAttributes redirectAttributes) {
        
        Customer newCustomer = new Customer();
        newCustomer.set(newCustomerDAO.getFirstName(), newCustomerDAO.getLastName(), 
                        newCustomerDAO.getTelephone(), newCustomerDAO.getAddress(), 
                        newCustomerDAO.getUserName(), newCustomerDAO.getPassword());
        
        customerRepository.save(newCustomer);

        redirectAttributes.addFlashAttribute("customerID", newCustomer.getId());
        return "redirect:/booking";
    }
}
