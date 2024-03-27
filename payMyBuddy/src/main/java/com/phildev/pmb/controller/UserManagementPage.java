package com.phildev.pmb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserManagementPage {

    @GetMapping("/user-management")
    public String renderUserManagementPage(){
        return "user-management";
    }
}
