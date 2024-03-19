package com.phildev.pmb.controller;


import com.phildev.pmb.model.User;
import com.phildev.pmb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String renderLoginPage(){
        return "login";
    }

    @GetMapping("/login-check")
    public String checkExistingUser(Principal principal, Model model){
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        if(user !=null){
            return "home";
        }
        model.addAttribute("userNotFound", String.format("No user found with email %s in the application", principal.getName()));
        logger.error("User does not exist in Pay My Buddy app with email : {}", principal.getName());
        return "login-check";
    }

    @GetMapping("/home")
    public String getUser(Principal principal, Model model){
        User user = userService.findUserByEmail(principal.getName());
        logger.info("User {} is logged to home page", user.getFirstName()+" "+user.getLastName());
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/admin")
    public String getAdmin(Principal principal, Model model){
        User user = userService.findUserByEmail(principal.getName());
        logger.info("Admin user {} is logged to admin page", user.getFirstName()+" "+user.getLastName());
        model.addAttribute("user", user);
        return "admin";
    }
}
