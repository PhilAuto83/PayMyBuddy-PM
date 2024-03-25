package com.phildev.pmb.controller;


import com.phildev.pmb.model.User;
import com.phildev.pmb.service.AccountService;
import com.phildev.pmb.service.UserService;
import com.phildev.pmb.utils.UserInfoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String renderLoginPage(){
        return "login";
    }

    @GetMapping("/forbidden-access")
    public String renderForbiddenAccessPage(){
        return "forbidden-access";
    }

    @GetMapping("/login-check")
    public String checkExistingUser(@AuthenticationPrincipal OidcUser oidcUser,Principal principal, Model model){
        String email = UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        model.addAttribute("email", email);
        User user = userService.findUserByEmail(email);
        if(email != null){
            if(userService.findUserByEmail(email)!= null){
                model.addAttribute("oidcUser", user.getFirstName());
                return "home";
            }

            logger.error("User with email {} does not exist in Pay My Buddy app.", email);

            return "forbidden-access";
        }
        logger.error("User does not exist in Pay My Buddy app");

       return "forbidden-access";
    }

    @GetMapping("/home")
    public String renderHomePage(@AuthenticationPrincipal OidcUser oidcUser, Principal principal, Model model){
        String email = UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        User user = userService.findUserByEmail(email);
        model.addAttribute("balance", accountService.getCurrentBalanceByUserEmail(email));
        logger.info("User {} is logged to home page", user.getFirstName()+" "+user.getLastName());
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/admin")
    public String renderAdminPage(@AuthenticationPrincipal OidcUser oidcUser, Principal principal, Model model){
        User user = userService.findUserByEmail(UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal));
        logger.info("Admin user {} is logged to admin page", user.getFirstName()+" "+user.getLastName());
        model.addAttribute("user", user);
        return "admin";
    }
}
