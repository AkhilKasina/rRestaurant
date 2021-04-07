package com.softwarestudiogroup1.uts.eRestaurant.controllers.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final String HOME_RESTAURANT = "homeRestaurant";

    @GetMapping("/")
    public String welcomeScreen() {
        return HOME_RESTAURANT;
    }
    
}
