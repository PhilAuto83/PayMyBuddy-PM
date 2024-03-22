package com.phildev.pmb.controller;

import com.phildev.pmb.model.Connection;
import com.phildev.pmb.model.User;
import com.phildev.pmb.service.ConnectionService;
import com.phildev.pmb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ConnectionController {

    private static Logger logger = LoggerFactory.getLogger(ConnectionController.class);

    @Autowired
    UserService userService;

    @Autowired
    ConnectionService connectionService;

    public Map<String, String> getConnectionsInfo(Principal principal){
        List<Connection> connections = connectionService.findAllConnectionsFromCurrentUser(principal.getName());
        Map<String, String> connectionNames = new HashMap<>();
        connections.forEach(connection -> {
            User user = userService.findUserByEmail(connection.getRecipientEmail());
            connectionNames.put(connection.getRecipientEmail(), user.getFirstName()+" "+user.getLastName());
        });
        return connectionNames;
    }



    @GetMapping("/connection")
    public String renderConnectionPage(Model model, Principal principal){
        model.addAttribute("connectionNames", getConnectionsInfo(principal));
        return "connection";
    }

    @GetMapping("/connection/search")
    public String getBuddyByEmail(Model model, Principal principal, @RequestParam String email){
        model.addAttribute("connectionNames", getConnectionsInfo(principal));
        User user = userService.findUserByEmail(email);
        User userToDisplay;
        model.addAttribute("principal", principal);
        model.addAttribute("user", user);
        if(user == null){
            logger.debug("Unknown or empty email address : {}", email);
            model.addAttribute("unknown", "Unknown or empty email address");
        }else if(user.getEmail().equals(principal.getName())){
            logger.debug("You cannot add a connection with your own address");
            model.addAttribute("error", "You cannot add a connection with your own address");
        }else if(connectionService.findByRecipientEmail(principal.getName(), email) != null
                && connectionService.findByRecipientEmail(principal.getName(), email).getSenderEmail().equals(principal.getName())){
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
    public String saveConnection(Model model, Principal principal, @RequestParam String email){
        model.addAttribute("connectionNames", getConnectionsInfo(principal));
        model.addAttribute("connections", connectionService.findAllConnectionsFromCurrentUser(principal.getName()));
        connectionService.save(new Connection(principal.getName(), email));
        return "redirect:/connection";
    }


}
