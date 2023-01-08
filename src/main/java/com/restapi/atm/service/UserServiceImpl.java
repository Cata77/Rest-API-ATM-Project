package com.restapi.atm.service;

import com.restapi.atm.exception.UserAlreadyExistsException;
import com.restapi.atm.exception.UserNotFoundException;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.repository.AccountRepository;
import com.restapi.atm.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public BankUser registerUser(BankUser bankUser) {
        userRepository.findBankUserByUserName(bankUser.getUserName())
                .ifPresent(s -> { throw new UserAlreadyExistsException("User already exists!");});

        Account account = new Account();
        account.setBankUser(bankUser);
        accountRepository.save(account);
        userRepository.save(bankUser);
        return bankUser;
    }

    @Override
    public BankUser loginUser(BankUser bankUser) {
        return userRepository.findBankUserByUserNameAndPassword(bankUser.getUserName(), bankUser.getPassword())
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Bad credentials!"));
    }

    @Override
    public Account getUserDetails(Integer id) {
        return accountRepository.findAccountByBankUserId(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
