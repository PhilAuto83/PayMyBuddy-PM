package com.phildev.pmb.controller;


import com.phildev.pmb.model.User;
import com.phildev.pmb.service.UserService;
import com.phildev.pmb.utils.UserInfoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    UserService userService;

    @GetMapping("/profile")
    public String renderProfilePage(@AuthenticationPrincipal OidcUser oidcUser, Principal principal, Model model){
        String email = UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        model.addAttribute("user", userService.findUserByEmail(email));
        return "profile";
    }


}
