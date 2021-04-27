package com.softwarestudiogroup1.uts.eRestaurant.controllers.customer;

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
public class OrderController {

    @GetMapping("/orderPage")
    public String orderPage(Model model) {
        return ViewManager.CUS_ORDER;
    }

    @GetMapping("/orderConfirmation")
    public String orderConfirmation(Model model) {
        return ViewManager.CUS_CONFIRM;
    }
}