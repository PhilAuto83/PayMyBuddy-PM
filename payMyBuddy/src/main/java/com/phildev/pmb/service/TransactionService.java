package com.phildev.pmb.service;

import com.phildev.pmb.model.Connection;
import com.phildev.pmb.model.Transaction;
import com.phildev.pmb.repository.AccountRepository;
import com.phildev.pmb.repository.ConnectionRepository;
import com.phildev.pmb.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    public List<Transaction> getTransactionsByUserEmail(String email){
        return transactionRepository.getTransactionsByEmail(email);
    };

    public double removeMoneyFromAccountWithFee(String email, Transaction transaction){
        BigDecimal preciseAmount = BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email));
        BigDecimal newAmount = preciseAmount.subtract(BigDecimal.valueOf(transaction.getAmount()).multiply(BigDecimal.valueOf(1.005)));
        return (double) Math.round(newAmount.doubleValue() * 100) /100;
    }

    public double addMoneyToAccount(String email, Transaction transaction){
        BigDecimal preciseAmount = BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email)).add(BigDecimal.valueOf(transaction.getAmount()));
        return  preciseAmount.doubleValue();
    }

    public boolean checkUserBalance(String email, Transaction transaction){
        BigDecimal accountAmount = BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email));
        BigDecimal transactionAmount = BigDecimal.valueOf(transaction.getAmount()).multiply(BigDecimal.valueOf(1.005));
        return  accountAmount.doubleValue()>=transactionAmount.doubleValue();
    }

    @Transactional
    public Transaction save(Transaction transaction){
        if(transaction.getConnectionId()==0){
            throw new RuntimeException("You have not selected any connection");
        }else if(transaction.getDescription()==null || transaction.getDescription().isBlank()){
            throw new RuntimeException("Description cannot be null or empty");
        }
        Optional<Connection> connection = connectionRepository.findById(transaction.getConnectionId());
        String senderEmail = "";
        String recipientEmail = "";
        if(connection.isPresent()){
            senderEmail = connection.get().getSenderEmail();
            recipientEmail = connection.get().getRecipientEmail();
            if(checkUserBalance(senderEmail, transaction)){
                double newSenderBalance = removeMoneyFromAccountWithFee(senderEmail, transaction);
                double newRecipientBalance = addMoneyToAccount(recipientEmail, transaction);
                accountRepository.updateAccountBalance(newSenderBalance, senderEmail);
                logger.debug("Balance for {} has been updated and is now {}", senderEmail, newSenderBalance);
                accountRepository.updateAccountBalance(newRecipientBalance, recipientEmail);
                logger.debug("Balance for {} has been updated and is now {}", recipientEmail, newRecipientBalance);
            }else{
                throw new RuntimeException("Insufficient amount in current user balance");
            }
        }else {
           logger.error("User and Recipient were not found so payment is aborted");
           throw new RuntimeException("User and Recipient were not found so payment is aborted");
        }
        return transactionRepository.save(transaction);
    }

}
