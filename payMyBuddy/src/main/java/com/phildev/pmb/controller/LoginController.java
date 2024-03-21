package com.phildev.pmb.controller;


import com.phildev.pmb.model.User;
import com.phildev.pmb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Map;

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
    public String checkExistingUser(@AuthenticationPrincipal OidcUser oidcUser, Model model){
        Map<String, Object> claims = oidcUser.getAttributes();
        String email = (String) claims.get("email");
        if(email != null){
            if(userService.findUserByEmail(email)!= null){
                model.addAttribute("user", oidcUser);
                return "home";
            }

            logger.error("User with email {} does not exist in Pay My Buddy app.", email);
            model.addAttribute("email", email);
            return "403";
        }
        logger.error("User with does not exist in Pay My Buddy app");

       return "403";
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
