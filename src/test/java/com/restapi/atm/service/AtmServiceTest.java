package com.restapi.atm.service;

import com.restapi.atm.model.Account;
import com.restapi.atm.repository.AccountRepository;
import com.restapi.atm.repository.TransactionRepository;
import com.restapi.atm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtmServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    private AtmService atmService;

    @BeforeEach
    void setUp() {
        atmService = new AtmService(accountRepository, userRepository, transactionRepository);
    }

    @Test
    void getAllUsers() {
        atmService.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsersAccount() {
        atmService.getAllUsersAccount();
        verify(accountRepository).findAll();
    }

    @Test
    void getAllTransactions() {
        atmService.getAllTransactions();
        verify(transactionRepository).findAll();
    }

    @Test
    void getHighestAccountBalance() {
        atmService.getHighestAccountBalance();
        verify(accountRepository).findUserWithHighestBalance();
    }

    @Test
    void calculateBankBalance() {
        Account account1 = new Account();
        account1.setBalance(BigDecimal.valueOf(100));

        Account account2 = new Account();
        account2.setBalance(BigDecimal.valueOf(200));

        Account account3 = new Account();
        account3.setBalance(BigDecimal.valueOf(300));

        List<Account> accounts = List.of(account1, account2, account3);
        when(accountRepository.findAll()).thenReturn(accounts);

        BigDecimal bankBalance = atmService.calculateBankBalance();

        assertEquals(BigDecimal.valueOf(600), bankBalance);
    }

    @Test
    void findUserWithMostTransactions() {
    }

    @Test
    void findDateWithMostTransactions() {
    }

    @Test
    void getTransactionsBetweenDate() {
    }
}