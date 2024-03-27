package com.phildev.pmb.service;

import com.phildev.pmb.model.Account;
import com.phildev.pmb.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

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
}
