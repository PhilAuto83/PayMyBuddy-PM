package com.phildev.pmb.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/pmb/user")
    public String getUser(){
        return "Welcome User";
    }

    @GetMapping("/pmb/admin")
    public String getAdmin(){
        return "Welcome Admin";
    }
}
