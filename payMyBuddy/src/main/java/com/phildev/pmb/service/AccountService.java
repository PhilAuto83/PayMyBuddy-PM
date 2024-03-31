package com.phildev.pmb.service;

import com.phildev.pmb.model.Account;
import com.phildev.pmb.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

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
        logger.info("Retrieve  balance for user {}", email);
        return accountRepository.getBalanceByUserEmail(email);
    }

    public void updateAccountBalance(double amount, String email){
        logger.info("Launching update user balance to {} for email : {}",amount, email);
        accountRepository.updateAccountBalance(amount, email);
    }

    public boolean hasSufficientMoney(String email, double amount){
        logger.info("Check if user has sufficient money to send it out to external account");
        BigDecimal preciseAmount = BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email));
        BigDecimal moneyOut = BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(1.005));
        return !(moneyOut.doubleValue() > preciseAmount.doubleValue());
    }

    @Transactional(rollbackOn = {Exception.class, SQLException.class, RuntimeException.class})
    public void depositMoney(double amount, String email){
        BigDecimal currentBalance =  BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email));
        BigDecimal deposit = currentBalance.add(BigDecimal.valueOf(amount));
        logger.info("Deposit {} € for user : {}",deposit.doubleValue(), email);
        accountRepository.updateAccountBalance(deposit.doubleValue(), email);
    }

    @Transactional(rollbackOn = {Exception.class, SQLException.class, RuntimeException.class})
    public void sendMoneyOut(double amount, String email){
        BigDecimal currentBalance =  BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email));
        BigDecimal newAccountBalance = currentBalance.subtract(BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(1.005)));
        double balanceUpdated = (double) Math.round(newAccountBalance.doubleValue() * 100) /100;
        logger.info("Withdrawal {} € for user : {}",amount,  email);
        accountRepository.updateAccountBalance(balanceUpdated, email);
        logger.info("New balance {} € for user : {}",balanceUpdated,  email);
    }
}
