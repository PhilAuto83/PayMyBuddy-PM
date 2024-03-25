package com.phildev.pmb.service;

import com.phildev.pmb.dto.TransactionDTO;
import com.phildev.pmb.model.Connection;
import com.phildev.pmb.model.Transaction;
import com.phildev.pmb.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByUserEmail(String email){
        return transactionRepository.getTransactionsByEmail(email);
    };
    @Transactional
    public Transaction save(Transaction transaction){
        if(transaction.getConnectionId()==0){
            throw new RuntimeException("You have not selected any connection");
        }else if(transaction.getDescription()==null || transaction.getDescription().isBlank()){
            throw new RuntimeException("Description cannot be null or empty");
        }
        return transactionRepository.save(transaction);
    }

}
