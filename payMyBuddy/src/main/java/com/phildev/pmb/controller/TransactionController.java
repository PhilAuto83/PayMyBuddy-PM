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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TransactionController {

    @Autowired
    UserService userService;

    @Autowired
    ConnectionService connectionService;


    @Autowired
    TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public List<TransactionDTO> getTransactionsInfo(@AuthenticationPrincipal OidcUser oidcUser, Principal principal){
        String email = UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        List<Transaction>transactions = transactionService.getTransactionsByUserEmail(email);
        Map<Integer, String> connections = getConnectionsNamesWithId(oidcUser, principal);
        return transactions.stream()
                .map(transaction -> {
                    for(Map.Entry<Integer, String> connection : connections.entrySet()){
                        if(transaction.getConnectionId()==connection.getKey()){
                            return new TransactionDTO(connection.getValue(), transaction.getDescription(), transaction.getAmount());
                        }
                    }
                    return null;
                }).toList();
    }

    public Map<Integer, String> getConnectionsNamesWithId(@AuthenticationPrincipal OidcUser oidcUser,  Principal principal){
        List<Connection> connections = connectionService.findAllConnectionsFromCurrentUser(UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal));
        Map<Integer, String> connectionNamesWithId = new HashMap<>();
        connections.forEach(connection -> {
            User user = userService.findUserByEmail(connection.getRecipientEmail());
            connectionNamesWithId.put(connection.getId(), user.getFirstName()+" "+user.getLastName());
        });
        return connectionNamesWithId;
    }

    @GetMapping("/transfer/info")
    public String getTransactionsByPage(@AuthenticationPrincipal OidcUser oidcUser, Principal principal,
                                        Model model, @RequestParam("pageNo") Optional<Integer> pageNo){
        String email = UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        User user = userService.findUserByEmail(email);
        Map<Integer, String> connections = getConnectionsNamesWithId(oidcUser, principal);
        int pageSize = 3;
        int currentPage = pageNo.orElse(1);
        logger.info("Display page {} with 3 transactions per page", currentPage);
        Page <TransactionDTO> page = transactionService.findPaginated(email, currentPage, pageSize);

        model.addAttribute("user", user);
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("connections", connections);
        model.addAttribute("currentPage", currentPage);
        logger.info("Current page is page {}", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        logger.info("Number of pages  is {}", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        logger.info("Number of transactions for user {} is {}", user.getEmail(), page.getTotalElements());
        model.addAttribute("transactionsInfo", page.getContent());
        logger.debug("Transaction list for user {}  when requesting page {} has size {}", user.getEmail(), currentPage, page.getContent().size());
        return "transfer";
    }

    @GetMapping(value ={"/transfer","/transfer/{pageNo}"})
    public String renderTransferPage(@AuthenticationPrincipal OidcUser oidcUser, Principal principal, Model model, @PathVariable(value = "pageNo", required = false)Optional<Integer> pageNo){

        if(pageNo.isPresent()){
            logger.info("Page number requested is not null : {}", pageNo.get());
            return getTransactionsByPage(oidcUser, principal, model, pageNo);
        }
        logger.info("Page number requested is null and is set to Optional.of(1)");
        return getTransactionsByPage(oidcUser, principal, model, Optional.of(1));
    }

    @PostMapping(value = "/transfer", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String saveTransaction(@AuthenticationPrincipal OidcUser oidcUser, Principal principal, @ModelAttribute Transaction transaction, Model model, RedirectAttributes redirectAttributes){
        String email = UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        Page <TransactionDTO> page = transactionService.findPaginated(email, 1, 3);
        //retrieve last page when creating new transaction
        int newPage = (int) (Math.round((double) page.getTotalElements() /3)+1);
        Transaction transactionSaved;
            try{
                transactionSaved = transactionService.save(transaction);
            }catch (Exception ex){
                redirectAttributes.addAttribute("true", true).addFlashAttribute("error", ex.getMessage());
                return "redirect:/transfer?error={true}";
            }
        return "redirect:/transfer/"+newPage;
    }
}
