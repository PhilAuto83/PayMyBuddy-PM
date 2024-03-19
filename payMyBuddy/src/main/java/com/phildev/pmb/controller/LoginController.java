package com.phildev.pmb.controller;


import com.phildev.pmb.model.User;
import com.phildev.pmb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LoginController {

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
        return "login-check";
    }

    @GetMapping("/home")
    public String getUser(Principal principal, Model model){
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/admin")
    public String getAdmin(Principal principal, Model model){
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "admin";
    }
}
