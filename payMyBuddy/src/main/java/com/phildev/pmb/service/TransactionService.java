package com.phildev.pmb.service;

import com.phildev.pmb.dto.TransactionDTO;
import com.phildev.pmb.model.Connection;
import com.phildev.pmb.model.Transaction;
import com.phildev.pmb.model.User;
import com.phildev.pmb.repository.AccountRepository;
import com.phildev.pmb.repository.ConnectionRepository;
import com.phildev.pmb.repository.TransactionRepository;
import com.phildev.pmb.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return  accountRepository.getBalanceByUserEmail(email) - transaction.getAmount()*1.005;
    }


    public double addMoneyToAccount(String email, Transaction transaction){
        return  accountRepository.getBalanceByUserEmail(email) + transaction.getAmount();
    }

    public boolean checkUserBalance(String email, Transaction transaction){
        return  accountRepository.getBalanceByUserEmail(email)>=transaction.getAmount()*1.005;
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
                accountRepository.updateAccountBalance(newRecipientBalance,senderEmail);
                logger.debug("Balance for {} has been updated and is now {}", senderEmail, newSenderBalance);
                accountRepository.updateAccountBalance(newRecipientBalance, recipientEmail);
                logger.debug("Balance for {} has been updated and is now {}", recipientEmail, newRecipientBalance);
            }else{
                throw new RuntimeException("Insufficient ammount in current user balance");
            }
        }else {
           logger.error("User and Recipient were not found so payment is aborted");
           throw new RuntimeException("User and Recipient were not found so payment is aborted");
        }
        return transactionRepository.save(transaction);
    }

}
