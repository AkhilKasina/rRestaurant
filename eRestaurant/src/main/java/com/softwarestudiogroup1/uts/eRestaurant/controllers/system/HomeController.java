package com.softwarestudiogroup1.uts.eRestaurant.controllers.system;

import com.softwarestudiogroup1.uts.eRestaurant.ViewManager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String welcomeScreen() {
        return ViewManager.HOME_RESTAURANT;
    }
    
}
