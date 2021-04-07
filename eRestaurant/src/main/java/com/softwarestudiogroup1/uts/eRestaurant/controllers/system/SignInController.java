package com.softwarestudiogroup1.uts.eRestaurant.controllers.system;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Customer;
import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Patron;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

        return "loginPage";
    }

    @PostMapping("/login")
    public String customerLogIn(@ModelAttribute("loginPatron") Patron patron, Model model, final RedirectAttributes redirectAttributes) {
        
        boolean success = false;
        int id = -1;

        List<Customer> cusLists = customerRepository.findAll();

        String username = patron.getUsername();
        String password = patron.getPassword();

        System.out.println("Login " +username + "  " + password);

        for (Customer currentCus: cusLists) {
            if (currentCus.getUsername().equals(username) && currentCus.getPassword().equals(password)) {
                success = true;
                id = currentCus.getId();
                break;
            }
        }
        
        System.out.println("is Success " + success);

        if (success == true) {
            redirectAttributes.addFlashAttribute("customerID", id);
            return "redirect:/booking";
        } 

        return "signupPage";
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "signupPage";
    }
}
