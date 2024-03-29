package com.phildev.pmb.service;

import com.phildev.pmb.dto.TransactionDTO;
import com.phildev.pmb.model.Connection;
import com.phildev.pmb.model.Transaction;
import com.phildev.pmb.repository.AccountRepository;
import com.phildev.pmb.repository.ConnectionRepository;
import com.phildev.pmb.repository.TransactionRepository;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
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
        logger.info("Search a list of transactions for user {}", email);
        return transactionRepository.getTransactionsByEmail(email);
    };

    public double removeMoneyFromAccountWithFee(String email, Transaction transaction){
        BigDecimal preciseAmount = BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email));
        BigDecimal newAmount = preciseAmount.subtract(BigDecimal.valueOf(transaction.getAmount()).multiply(BigDecimal.valueOf(1.005)));
        logger.debug("Try to remove money {} from user account {} with 0.5% fee", transaction.getAmount(), email);
        return (double) Math.round(newAmount.doubleValue() * 100) /100;
    }

    public double addMoneyToAccount(String email, Transaction transaction){
        BigDecimal preciseAmount = BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email)).add(BigDecimal.valueOf(transaction.getAmount()));
        logger.debug("Try to add money {} from user account {}", transaction.getAmount(), email);
        return  preciseAmount.doubleValue();
    }

    public boolean checkUserBalance(String email, Transaction transaction){
        BigDecimal accountAmount = BigDecimal.valueOf(accountRepository.getBalanceByUserEmail(email));
        BigDecimal transactionAmount = BigDecimal.valueOf(transaction.getAmount()).multiply(BigDecimal.valueOf(1.005));
        logger.info("Checking user {} has sufficient amount on his account", email);
        return  accountAmount.doubleValue()>=transactionAmount.doubleValue();
    }

    public Page<TransactionDTO> findPaginated(String email, int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo -1, pageSize);
            return transactionRepository.getTransactionsDTOByEmail(email, pageable);
    }

    @Transactional(rollbackOn = {SQLException.class, RuntimeException.class} )
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
                try{
                    accountRepository.updateAccountBalance(newSenderBalance, senderEmail);
                    logger.debug("Balance for {} has been updated and is now {}", senderEmail, newSenderBalance);
                    accountRepository.updateAccountBalance(newRecipientBalance, recipientEmail);
                    logger.debug("Balance for {} has been updated and is now {}", recipientEmail, newRecipientBalance);
                }catch(Exception ex){
                    logger.error("Something went wrong when updating sender {} or recipient account {}", senderEmail, recipientEmail);
                    throw new RuntimeException("Something went wrong when updating sender or recipient account");
                }
            }else{
                logger.error("Insufficient amount in current user balance");
                throw new RuntimeException("Insufficient amount in current user balance");
            }
        }else {
           logger.error("User and Recipient were not found so payment is aborted");
           throw new RuntimeException("User and Recipient were not found so payment is aborted");
        }
        return transactionRepository.save(transaction);
    }

}
