package com.phildev.pmb.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String renderLoginPage(){
        return "login";
    }

    @GetMapping("/home")
    public String getUser(){
        return "home";
    }

    @GetMapping("/admin")
    public String getAdmin(){
        return "admin";
    }
}
