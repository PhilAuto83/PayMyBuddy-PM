package com.phildev.pmb.controller;

import com.phildev.pmb.dto.TransactionDTO;
import com.phildev.pmb.model.Connection;
import com.phildev.pmb.model.Transaction;
import com.phildev.pmb.model.User;
import com.phildev.pmb.service.ConnectionService;
import com.phildev.pmb.service.TransactionService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TransactionController {

    @Autowired
    UserService userService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public Map<Integer, String> getConnectionsNamesWithId(@AuthenticationPrincipal OidcUser oidcUser,  Principal principal){
        List<Connection> connections = connectionService.findAllConnectionsFromCurrentUser(UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal));
        Map<Integer, String> connectionNamesWithId = new HashMap<>();
        connections.forEach(connection -> {
            User user = userService.findUserByEmail(connection.getRecipientEmail());
            connectionNamesWithId.put(connection.getId(), user.getFirstName()+" "+user.getLastName());
        });
        return connectionNamesWithId;
    }

    @GetMapping("/transfer")
    public String renderTransferPage(@AuthenticationPrincipal OidcUser oidcUser, Principal principal, Model model){
        String email = UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        User user = userService.findUserByEmail(email);
        List<Transaction>transactions = transactionService.getTransactionsByUserEmail(email);
        Map<Integer, String> connections = getConnectionsNamesWithId(oidcUser, principal);
        List<TransactionDTO> transactionDTOS = transactions.stream()
                .map(transaction -> {
                    for(Map.Entry<Integer, String> connection : connections.entrySet()){
                        if(transaction.getConnectionId()==connection.getKey()){
                            return new TransactionDTO(connection.getValue(), transaction.getDescription(), transaction.getAmount());
                        }
                    }
                    return null;
                }).toList();

        model.addAttribute("user", user);
        model.addAttribute("transactionsInfo", transactionDTOS);
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("connections", connections);
        return "transfer";
    }

    @PostMapping("/transfer")
    public String saveTransaction( @ModelAttribute Transaction transaction, Model model){
        Transaction transactionSaved;
            try{
                transactionSaved = transactionService.save(transaction);
            }catch (Exception ex){
                model.addAttribute("errors", ex.getMessage());
                return "transfer";
            }

        return "transfer";
    }
}
