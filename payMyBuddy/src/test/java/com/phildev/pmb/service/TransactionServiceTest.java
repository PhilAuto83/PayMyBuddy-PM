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

    @Test
    public void testUserBalanceHasEnoughMoney(){
        Transaction transaction = new Transaction();
        transaction.setAmount(69.5);
        transaction.setDescription("Movie Spider");
        transaction.setConnectionId(2);
        Assertions.assertTrue(transactionService.checkUserBalance("tester.cousy@test.fr", transaction));
    }

    @Test
    public void testUserBalanceHasNotEnoughMoney(){
        Transaction transaction = new Transaction();
        transaction.setAmount(200.0);
        transaction.setDescription("Movie Spider");
        transaction.setConnectionId(2);
        Assertions.assertFalse(transactionService.checkUserBalance("tester.cousy@test.fr", transaction));
    }

    @Test
    public void testMoneyIsAddedToRecipientAccount(){
        Transaction transaction = new Transaction();
        transaction.setAmount(50.0);
        double result = transactionService.addMoneyToAccount("testo.monkey@test.fr", transaction);
        Assertions.assertEquals(500.0, result);
    }

    @Test
    public void testMoneyIsSubstractedFromSenderAccount(){
        Transaction transaction = new Transaction();
        transaction.setAmount(50.0);
        double result = transactionService.removeMoneyFromAccountWithFee("testy@test.fr", transaction);
        Assertions.assertEquals(249.75, result);
    }

    @Test
    public void testExceptionIsThrownForEmptyDescription(){
        Transaction transaction = new Transaction();
        transaction.setAmount(50.0);
        transaction.setDescription("");
        Assertions.assertThrows(RuntimeException.class, ()->transactionService.save(transaction));
    }

    @Test
    public void testExceptionIsThrownFWhenNoConnectionIsSelected(){
        Transaction transaction = new Transaction();
        transaction.setAmount(50.0);
        transaction.setConnectionId(0);
        transaction.setDescription("Movie");
        Assertions.assertThrows(RuntimeException.class, ()->transactionService.save(transaction));
    }

    @Test
    public void testTransactionIsSavedWhenAmountIsSufficient(){
        Transaction transaction = new Transaction();
        transaction.setAmount(50.0);
        transaction.setConnectionId(1);
        transaction.setDescription("Movie");
        Transaction transactionSaved = transactionService.save(transaction);
        Assertions.assertEquals(50.0, transactionSaved.getAmount());
        Assertions.assertEquals("Movie", transactionSaved.getDescription());
        Assertions.assertEquals(1, transactionSaved.getConnectionId());
    }

    @Test
    public void testTransactionIsNotSavedWhenAmountIsInSufficient(){
        Transaction transaction = new Transaction();
        transaction.setAmount(5000.0);
        transaction.setConnectionId(1);
        transaction.setDescription("Movie");
        Assertions.assertThrows(RuntimeException.class, ()->transactionService.save(transaction));
    }

    @Test
    public void testTransactionIsNotSavedWhenConnectionIsNotFound(){
        Transaction transaction = new Transaction();
        transaction.setAmount(50.0);
        transaction.setConnectionId(9);
        transaction.setDescription("Movie");
        Assertions.assertThrows(RuntimeException.class, ()->transactionService.save(transaction));
    }

}
