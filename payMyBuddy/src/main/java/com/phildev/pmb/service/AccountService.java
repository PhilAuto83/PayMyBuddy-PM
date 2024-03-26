package com.phildev.pmb.service;

import com.phildev.pmb.model.Account;
import com.phildev.pmb.repository.AccountRepository;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    public Account save(Account account) throws Exception {
        Account accountSavedInDB;
        try{
            accountSavedInDB  = accountRepository.save(account);
        }catch(Exception ex){
            logger.error("Cannot create an account whose id {} already exists or does not match a user_id", account.getId());
            throw new Exception(String.format("Cannot create an account with id %s  which already exists or does not match a user_id", account.getId()));
        }
        return accountSavedInDB;
    }

    public double getCurrentBalanceByUserEmail(String email){
        return accountRepository.getBalanceByUserEmail(email);
    }

    public void updateAccountBalance(double amount, String email){
        accountRepository.updateAccountBalance(amount, email);
    }
}
