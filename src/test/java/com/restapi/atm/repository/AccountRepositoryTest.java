package com.restapi.atm.repository;

import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        account1 = new Account();
        account1.setBankUser(new BankUser());
        account1.setBalance(BigDecimal.valueOf(250));
        account1.setTransactions(List.of(new Transaction()));
        account2 = new Account();
        account2.setBankUser(new BankUser());
        account2.setBalance(BigDecimal.valueOf(420));
        account2.setTransactions(List.of(new Transaction()));
        accountRepository.saveAll(Arrays.asList(account1, account2));
    }

    @Test
    void findAccountByBankUserId() {
        Optional<Account> result = accountRepository.findAccountByBankUserId(1);
        assertTrue(result.isPresent());
        assertEquals(account1,result.get());
    }

    @Test
    void findUserWithHighestBalance() {
        Account result = accountRepository.findUserWithHighestBalance();
        assertEquals(account2,result);
    }
}