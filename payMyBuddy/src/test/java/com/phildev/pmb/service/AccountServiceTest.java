package com.phildev.pmb.service;

import com.phildev.pmb.model.Account;
import com.phildev.pmb.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void testAccountCreationFailsWithInvalidId() throws Exception {
        Assertions.assertThrows(Exception.class, ()->accountService.save(new Account(10)));
    }

    @Test
    public void testAccountBalanceRetrieved() throws Exception {
        Assertions.assertEquals(70.0, accountService.getCurrentBalanceByUserEmail("testee.money@test.fr"));
    }

    @Test
    public void testSufficientMoneyInAccountReturnsFalse() throws Exception {
        Assertions.assertFalse(accountService.hasSufficientMoney("testee.money@test.fr",5000));
    }

    @Test
    public void testSufficientMoneyInAccountReturnsTrue() throws Exception {
        Assertions.assertTrue(accountService.hasSufficientMoney("testee.money@test.fr",20));
    }

    @Test
    public void testUpdatingAccountForDepositReturnsCorrectBalance() throws Exception {
        accountService.depositMoney(20.0, "deposit@test.fr");
        Assertions.assertEquals(620.0, accountRepository.getBalanceByUserEmail("deposit@test.fr"));
    }

    @Test
    public void testUpdatingAccountWhenSendingMoneyOut() throws Exception {
        accountService.sendMoneyOut(50.0, "send@test.fr");
        Assertions.assertEquals(49.75, accountRepository.getBalanceByUserEmail("send@test.fr"));
    }

}
