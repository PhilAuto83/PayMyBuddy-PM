package com.phildev.pmb.controller;


import com.phildev.pmb.model.Account;
import com.phildev.pmb.model.User;
import com.phildev.pmb.service.AccountService;
import com.phildev.pmb.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/register")
    public String renderRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/registration-success")
    public String renderRegistrationSuccessPage(){
        return "registration-success";
    }


    @PostMapping("/register")
    public String createUser(@ModelAttribute User user, Model model) throws Exception {
        model.addAttribute("user", user);
        User userSaved;
        try {
            userSaved = userService.save(user);
        }catch(ConstraintViolationException ex){
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            List<String> errors = violations.stream()
                    .map(ConstraintViolation::getMessage).toList();
            model.addAttribute("errors", errors);
            return "register";
        }catch(Exception ex){
            model.addAttribute("errors", ex.getMessage());
            return "register";
        }
        logger.info("User {} has been saved in database.", user.getFirstName()+" "+user.getLastName());
        Account account = accountService.save(new Account(user.getId()));
        logger.info("Account with id {} has been created in database", account.getId());
        return "registration-success";
    }
}
