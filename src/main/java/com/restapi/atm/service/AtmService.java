package com.restapi.atm.service;

import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.repository.AccountRepository;
import com.restapi.atm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AtmService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AtmService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<BankUser> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Account> getAllUsersAccount() {
        return accountRepository.findAll();
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
}
