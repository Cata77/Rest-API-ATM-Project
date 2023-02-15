package com.restapi.atm.service;

import com.restapi.atm.exception.UserNotFoundException;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.model.Transaction;
import com.restapi.atm.repository.AccountRepository;
import com.restapi.atm.repository.TransactionRepository;
import com.restapi.atm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AtmService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AtmService(AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<BankUser> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Account> getAllUsersAccount() {
        return accountRepository.findAll();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Account getHighestAccountBalance() {
        return accountRepository.findUserWithHighestBalance();
    }

    public BigDecimal calculateBankBalance() {
        return accountRepository.findAll()
                .stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BankUser findUserWithMostTransactions() {
        return userRepository.findBankUserById(transactionRepository.getUserIdWithMostTransactions())
                .orElseThrow(() ->new UserNotFoundException("User not found!"));
    }

    public Date findDateWithMostTransactions() {
        return transactionRepository.getDateWithMostTransactions();
    }

    public List<Transaction> getTransactionsBetweenDate(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.getBankTransactionsBetweenDates(startDate, endDate);
    }
}
