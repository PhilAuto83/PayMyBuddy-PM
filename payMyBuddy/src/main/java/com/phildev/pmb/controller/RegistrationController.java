package com.phildev.pmb.controller;

import com.phildev.pmb.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegistrationController {

    @GetMapping("/register")
    public String renderRegistrationForm(){
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String createUser(@ModelAttribute User user, Model model){
        model.addAttribute("firstName", "");
        return "register-success";
    }
}
