package com.restapi.atm.service;

import com.restapi.atm.exception.UserAlreadyExistsException;
import com.restapi.atm.exception.UserNotFoundException;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.model.Transaction;
import com.restapi.atm.model.TransactionType;
import com.restapi.atm.repository.AccountRepository;
import com.restapi.atm.repository.TransactionRepository;
import com.restapi.atm.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private static final String NOT_FOUND_MESSAGE = "User not found!";

    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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
                .orElseThrow(() -> new UserNotFoundException(NOT_FOUND_MESSAGE));
    }

    @Override
    @Transactional
    public Transaction createDepositTransaction(Integer id, BigDecimal amount) {
        Account bankUserAccount = accountRepository.findAccountByBankUserId(id)
                .orElseThrow(() -> new UserNotFoundException(NOT_FOUND_MESSAGE));

        Transaction transaction = createTransaction(amount, TransactionType.DEPOSIT);
        bankUserAccount.getTransactions().add(transaction);
        bankUserAccount.setBalance(bankUserAccount.getBalance().add(amount));
        accountRepository.save(bankUserAccount);

        return transaction;
    }

    public Transaction createTransaction(BigDecimal amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setValue(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        return transaction;
    }
}
