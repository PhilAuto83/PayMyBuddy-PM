package com.phildev.pmb.controller;

import com.phildev.pmb.model.BankInfo;
import com.phildev.pmb.service.AccountService;
import com.phildev.pmb.utils.UserInfoUtility;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;

@Controller
public class DepositController {

    private static final Logger logger = LoggerFactory.getLogger(DepositController.class);

    @Autowired
    private AccountService accountService;

    @Value("${payment.platform}")
    private String paymentPlatformUrl;

    @PostMapping(value = "/deposit", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String deposit(@Valid @ModelAttribute BankInfo bankInfo, @AuthenticationPrincipal OidcUser oidcUser, Principal principal){
        String email = UserInfoUtility.getUserAuthenticatedEmail(oidcUser, principal);
        try{
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(paymentPlatformUrl + "/deposit");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<BankInfo> httpEntity = new HttpEntity<>(bankInfo, headers);

            ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);
            System.out.println(result);

            if(result.getStatusCode()== HttpStatusCode.valueOf(200)){
                accountService.depositMoney(bankInfo.getAmount(), email);
                logger.info("Deposit ended with success!!");
            }else{
                logger.error("Payment failed when contacting external bank account");
            }
        }catch(Exception ex){
            logger.error("An error occurred when contacting external bank account : {}", ex.getMessage());
           return "redirect:/home";
        }
        return "redirect:/home";
    }
}
