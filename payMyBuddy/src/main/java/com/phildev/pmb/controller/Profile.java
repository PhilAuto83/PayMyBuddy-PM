package com.phildev.pmb.controller;


import com.phildev.pmb.model.User;
import com.phildev.pmb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class Profile {

    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public String renderProfilePage(Model model, Principal principal){
        model.addAttribute("user", userService.findUserByEmail(principal.getName()));
        return "profile";
    }


}
