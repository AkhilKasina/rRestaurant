package com.softwarestudiogroup1.uts.eRestaurant.controllers.system;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Patron;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import javax.swing.text.View;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.CustomerRepository;

import org.springframework.ui.Model;

@Controller
public class SignInController {

    private final CustomerRepository customerRepository;

    public SignInController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @GetMapping("/login")
    public String signInScreen(Model model) {

        model.addAttribute("loginPatron", new Patron());

        return ViewManager.LOG_IN;
    }

    @PostMapping("/login")
    public String customerLogIn(@ModelAttribute("loginPatron") Patron patron, Model model, final RedirectAttributes redirectAttributes) {
        
        boolean success = false;
        int id = -1;

        String username = patron.getUsername();
        String password = patron.getPassword();

        System.out.println("Login " +username + "  " + password);

        if (username.startsWith("M") || username.startsWith("m")) {
            // Manager Login
        } 
        else if (username.startsWith("S") || username.startsWith("s")) {
            // Staff Login
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

        System.out.println("isLoginSuccess: " + success);

        

        // Display Error instead
        return ViewManager.SIGN_UP;
    }

    @GetMapping("/signup")
    public String signUpPage(Model model) {
        model.addAttribute("signupCustomer", new NewCustomerDAO());
        return ViewManager.SIGN_UP;
    }

    @PostMapping("/signup")
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
