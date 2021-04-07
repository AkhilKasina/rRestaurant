package com.softwarestudiogroup1.uts.eRestaurant.controllers.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInController {
    
    @GetMapping("/login")
    public String signInScreen() {
        return "loginPage";
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "signupPage";
    }
}
