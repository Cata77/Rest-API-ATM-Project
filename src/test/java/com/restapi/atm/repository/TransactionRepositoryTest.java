package com.restapi.atm.repository;

import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.model.Transaction;
import com.restapi.atm.model.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        Transaction transaction1 = new Transaction();
        transaction1.setTimestamp(LocalDateTime.of(2023,2,26,7,0));
        transaction1.setValue(BigDecimal.valueOf(150));
        transaction1.setTransactionType(TransactionType.DEPOSIT);

        Transaction transaction2 = new Transaction();
        transaction2.setTimestamp(LocalDateTime.of(2023,2,26,8,0));
        transaction2.setValue(BigDecimal.valueOf(50));
        transaction2.setTransactionType(TransactionType.WITHDRAW);

        Transaction transaction3 = new Transaction();
        transaction3.setTimestamp(LocalDateTime.of(2023,2,26,9,0));
        transaction3.setValue(BigDecimal.valueOf(200));
        transaction3.setTransactionType(TransactionType.TRANSFER);

        account1 = new Account();
        account1.setBankUser(new BankUser());
        account1.setBalance(BigDecimal.valueOf(250));
        account1.setTransactions(List.of(transaction1, transaction2));

        account2 = new Account();
        account2.setBankUser(new BankUser());
        account2.setBalance(BigDecimal.valueOf(250));
        account2.setTransactions(List.of(transaction3));

        accountRepository.saveAll(List.of(account1,account2));
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3));
    }

    @Test
    void getUserIdWithMostTransactions() {
        Integer result = transactionRepository.getUserIdWithMostTransactions();
        assertEquals(account1.getId(),result);
    }

    @Test
    void getTransactionsByAccountId() {
        List<Transaction> result = transactionRepository.getTransactionsByAccountId(1);
        Assertions.assertIterableEquals(account1.getTransactions(), result);
    }

    @Test
    void getUserTransactionsBetweenDates() {
        LocalDateTime start = LocalDateTime.of(2023,2,26,5,0);
        LocalDateTime end = LocalDateTime.of(2023,2,26,10,0);
        List<Transaction> result = transactionRepository.getUserTransactionsBetweenDates(2,start,end);
        Assertions.assertIterableEquals(account2.getTransactions(), result);
    }

    @Test
    void getBankTransactionsBetweenDates() {
        LocalDateTime start = LocalDateTime.of(2023,2,26,5,0);
        LocalDateTime end = LocalDateTime.of(2023,2,26,10,0);
        List<Transaction> result = transactionRepository.getBankTransactionsBetweenDates(start,end);
        Assertions.assertIterableEquals(transactionRepository.findAll(), result);
    }
}