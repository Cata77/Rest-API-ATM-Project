package com.restapi.atm.service;

import com.restapi.atm.repository.AccountRepository;
import com.restapi.atm.repository.TransactionRepository;
import com.restapi.atm.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AtmServiceTest {
    @Mock private AccountRepository accountRepository;
    @Mock private UserRepository userRepository;
    @Mock private TransactionRepository transactionRepository;
    private AtmService atmService;

    @BeforeEach
    void setUp() {
        atmService = new AtmService(accountRepository,userRepository,transactionRepository);
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
    }

    @Test
    void getHighestAccountBalance() {
    }

    @Test
    void calculateBankBalance() {
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