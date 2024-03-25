package com.phildev.pmb.service;

import com.phildev.pmb.model.Transaction;
import com.phildev.pmb.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionService transactionService;

    @Test
    public void testListReturnForKnownUser(){
        List<Transaction> transactions = transactionService.getTransactionsByUserEmail("test.pmb@test.fr");
        Assertions.assertEquals(1, ((List<?>) transactions).size());
        Assertions.assertEquals(15.0, transactions.getFirst().getAmount());
        Assertions.assertEquals(1, transactions.getFirst().getConnectionId());
        Assertions.assertEquals("Movie tickets", transactions.getFirst().getDescription());
    }
    @Test
    public void testListEmptyForUnKnownUser(){
        Assertions.assertEquals(Collections.emptyList(), transactionService.getTransactionsByUserEmail("testee.money@test.fr"));
    }

}
