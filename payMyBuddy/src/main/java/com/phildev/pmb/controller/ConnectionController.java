package com.phildev.pmb.controller;

import com.phildev.pmb.model.Connection;
import com.phildev.pmb.model.User;
import com.phildev.pmb.service.ConnectionService;
import com.phildev.pmb.service.UserService;
import com.phildev.pmb.utils.UserInfoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ConnectionController {

    private static final  Logger logger = LoggerFactory.getLogger(ConnectionController.class);

    @Autowired
    UserService userService;

    @Autowired
    ConnectionService connectionService;

    public Map<String, String> getConnectionsInfo(@AuthenticationPrincipal OidcUser oidcUser,  Principal principal){
        List<Connection> connections = connectionService.findAllConnectionsFromCurrentUser(UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal));
        Map<String, String> connectionNames = new HashMap<>();
        connections.forEach(connection -> {
            User user = userService.findUserByEmail(connection.getRecipientEmail());
            connectionNames.put(connection.getRecipientEmail(), user.getFirstName()+" "+user.getLastName());
        });
        return connectionNames;
    }

    @GetMapping("/connection")
    public String renderConnectionPage(Model model, @AuthenticationPrincipal OidcUser oidcUser,  Principal principal){
        model.addAttribute("connectionNames", getConnectionsInfo(oidcUser, principal));
        return "connection";
    }

    @GetMapping("/connection/search")
    public String getBuddyByEmail(Model model, @AuthenticationPrincipal OidcUser oidcUser, Principal principal, @RequestParam String email){
        model.addAttribute("connectionNames", getConnectionsInfo(oidcUser, principal));
        String userConnectedEmail =  UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        User user = userService.findUserByEmail(email);
        User userToDisplay;
        model.addAttribute("principal", principal);
        model.addAttribute("user", user);
        if(user == null){
            logger.debug("Unknown or empty email address : {}", email);
            model.addAttribute("unknown", "Unknown or empty email address");
        }else if(user.getEmail().equals(userConnectedEmail)){
            logger.debug("You cannot add a connection with your own address");
            model.addAttribute("error", "You cannot add a connection with your own address");
        }else if(connectionService.findByRecipientEmail(userConnectedEmail, email) != null
                && connectionService.findByRecipientEmail(userConnectedEmail, email).getSenderEmail().equals(userConnectedEmail)){
            logger.debug("You cannot add an existing connection with {}", email);
            model.addAttribute("error", "You cannot add an existing connection with "+email);
        }else{
            userToDisplay = user;
            model.addAttribute("success","true");
            model.addAttribute("userToDisplay", userToDisplay.getFirstName()+" "+userToDisplay.getLastName());
            model.addAttribute("recipientEmail", userToDisplay.getEmail());
            logger.debug("Search for a connection buddy found with user : {}", userToDisplay.getFirstName()+" "+userToDisplay.getLastName());
            model.addAttribute("buddyName", userToDisplay.getFirstName()+" "+userToDisplay.getLastName());
        }
        return "connection";
    }

    @PostMapping(value="/connection", consumes={MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String saveConnection(Model model, @AuthenticationPrincipal OidcUser oidcUser, Principal principal, @RequestParam String email){
        String userConnectedEmail =  UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        model.addAttribute("connectionNames", getConnectionsInfo(oidcUser, principal));
        model.addAttribute("connections", connectionService.findAllConnectionsFromCurrentUser(userConnectedEmail));
        connectionService.save(new Connection(userConnectedEmail, email));
        return "redirect:/connection";
    }


}
