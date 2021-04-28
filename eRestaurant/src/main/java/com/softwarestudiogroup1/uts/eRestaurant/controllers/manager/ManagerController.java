package com.softwarestudiogroup1.uts.eRestaurant.controllers.manager;

import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.ManagerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.StaffRepository;
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

    public ManagerController(ManagerRepository managerRepository, StaffRepository staffRepository) {
        this.managerRepository = managerRepository;
        this.staffRepository = staffRepository;

    }
    
    @GetMapping("/manager")
    public String managerPortal(@ModelAttribute("managerID") int managerID, Model model) {
        this.currentID = managerID;
        Optional<Manager> currentManager = managerRepository.findById(this.currentID);

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
