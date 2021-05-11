package com.softwarestudiogroup1.uts.eRestaurant.controllers.manager;

import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;
import com.softwarestudiogroup1.uts.eRestaurant.models.ManagerRepository;
import com.softwarestudiogroup1.uts.eRestaurant.models.StaffRepository;
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

    public StaffController(ManagerRepository managerRepository, StaffRepository staffRepository) {
        this.managerRepository = managerRepository;
        this.staffRepository = staffRepository;

    }
    
    @GetMapping("staff")
    public String staffPortal(@ModelAttribute("staffID") int staffID, Model model) {
        this.currentID = staffID;
        Optional<Staff> currentStaff = staffRepository.findById(this.currentID);

        if (currentStaff.isPresent()) {
            Staff staff = currentStaff.get();
            model.addAttribute("staff", staff);
        }

        return "";
    }
}
