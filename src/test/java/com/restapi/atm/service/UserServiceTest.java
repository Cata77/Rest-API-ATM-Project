package com.restapi.atm.service;

import com.restapi.atm.dto.AuthenticatedUserDto;
import com.restapi.atm.exception.LowBalanceException;
import com.restapi.atm.exception.UserAlreadyExistsException;
import com.restapi.atm.exception.UserNotFoundException;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.model.Transaction;
import com.restapi.atm.model.TransactionType;
import com.restapi.atm.repository.AccountRepository;
import com.restapi.atm.repository.TransactionRepository;
import com.restapi.atm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Spy
    private UserRepository userRepository;
    @Spy
    private AccountRepository accountRepository;
    @Spy
    private TransactionRepository transactionRepository;
    private AuthenticatedUserDto userDto;
    private BankUser bankUser1;
    private Account account1;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        userDto = new AuthenticatedUserDto();
        userDto.setUserName("User 1");
        userDto.setPassword("testPass");

        bankUser1 = new BankUser();
        bankUser1.setUserName("User 1");
        bankUser1.setPassword("testPass");

        account1 = new Account();
        account1.setBankUser(bankUser1);
        account1.setBalance(BigDecimal.valueOf(250));
        account1.setTransactions(List.of(new Transaction()));

        BankUser bankUser2 = new BankUser();
        bankUser2.setUserName("User 3");
        bankUser2.setPassword("testPass");

        transaction = new Transaction();
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setValue(BigDecimal.valueOf(100));
        transaction.setTransactionType(TransactionType.DEPOSIT);

        userRepository.saveAll(List.of(bankUser1,bankUser2));
    }

    @Test
    void registerUser() {
        Account account = new Account();
        account.setBankUser(bankUser1);

        when(userRepository.findBankUserByUserName(userDto.getUserName())).thenReturn(Optional.empty());

        userService.registerUser(userDto);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account capturedAccount = accountArgumentCaptor.getValue();
        assertEquals(capturedAccount,account);

        ArgumentCaptor<BankUser> bankUserArgumentCaptor = ArgumentCaptor.forClass(BankUser.class);
        verify(userRepository).save(bankUserArgumentCaptor.capture());
        BankUser capturedBankUser = bankUserArgumentCaptor.getValue();
        assertEquals(capturedBankUser,bankUser1);
    }

    @Test
    void registerUserThrowsException() {
        when(userRepository.findBankUserByUserName("User 1"))
                .thenReturn(Optional.ofNullable(bankUser1));
        assertThatThrownBy(() -> userService.registerUser(userDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User already exists!");
    }

    @Test
    void loginUser() {
        when(userRepository.findBankUserByUserNameAndPassword("User 1", "testPass"))
                .thenReturn(Optional.of(bankUser1));
        BankUser result = userService.loginUser(userDto);
        assertEquals(bankUser1,result);
    }

    @Test
    void loginUserThrowsException() {
        when(userRepository.findBankUserByUserNameAndPassword("User 1", "wrongPass"))
                .thenReturn(null);
        assertThatThrownBy(() -> userService.loginUser(userDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Bad credentials!");
    }

    @Test
    void getUserDetails() {
        when(accountRepository.findAccountByBankUserId(1)).thenReturn(Optional.of(account1));
        Account result = userService.getUserDetails(1);
        assertEquals(account1,result);
    }

    @Test
    void getUserDetailsThrowsException() {
        when(accountRepository.findAccountByBankUserId(10)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserDetails(10))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found!");
    }

    @Test
    void createDepositTransaction() {
        Account account = new Account();
        account.setTransactions(new ArrayList<>());
        when(accountRepository.findAccountByBankUserId(1)).thenReturn(Optional.of(account));
        userService.createDepositTransaction(1,BigDecimal.valueOf(100));
        account.getTransactions().add(transaction);
        account.setBalance(account.getBalance().add(BigDecimal.valueOf(100)));

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account capturedAccount = accountArgumentCaptor.getValue();
        assertEquals(capturedAccount,account);
    }

    @Test
    void createDepositTransactionThrowsException() {
        when(accountRepository.findAccountByBankUserId(10)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.createDepositTransaction(10,BigDecimal.valueOf(100)))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found!");
    }

    @Test
    void createWithdrawTransaction() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(200));
        account.setTransactions(new ArrayList<>());
        when(accountRepository.findAccountByBankUserId(1)).thenReturn(Optional.of(account));
        userService.createWithdrawTransaction(1,BigDecimal.valueOf(100));
        transaction.setTransactionType(TransactionType.WITHDRAW);
        account.getTransactions().add(transaction);
        account.setBalance(account.getBalance().subtract(BigDecimal.valueOf(100)));

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account capturedAccount = accountArgumentCaptor.getValue();
        assertEquals(capturedAccount,account);
    }

    @Test
    void createWithdrawTransactionThrowsUserNotFoundException() {
        when(accountRepository.findAccountByBankUserId(10)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.createWithdrawTransaction(10,BigDecimal.valueOf(100)))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found!");
    }

    @Test
    void createWithdrawTransactionThrowsLowBalanceException() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(10));
        account.setTransactions(new ArrayList<>());
        when(accountRepository.findAccountByBankUserId(1)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> userService.createWithdrawTransaction(1,BigDecimal.valueOf(100)))
                .isInstanceOf(LowBalanceException.class)
                .hasMessage("Insufficient funds!");
    }

    @Test
    void createTransferTransaction() {
    }

    @Test
    void realizeTransferTransaction() {
        Account senderAccount = new Account();
        senderAccount.setId(1);
        senderAccount.setBalance(BigDecimal.valueOf(200));
        senderAccount.setTransactions(new ArrayList<>());

        Account beneficiaryAccount = new Account();
        beneficiaryAccount.setId(1);
        beneficiaryAccount.setTransactions(new ArrayList<>());

        Transaction sendTransaction = new Transaction();
        sendTransaction.setTimestamp(LocalDateTime.now());
        sendTransaction.setValue(BigDecimal.valueOf(100));
        sendTransaction.setTransactionType(TransactionType.TRANSFER);
        sendTransaction.setFromIdAccount(senderAccount.getId());
        sendTransaction.setToIdAccount(beneficiaryAccount.getId());
        senderAccount.getTransactions().add(sendTransaction);
        senderAccount.setBalance(senderAccount.getBalance().subtract(BigDecimal.valueOf(100)));

        Transaction receiveTransaction = new Transaction();
        receiveTransaction.setTimestamp(LocalDateTime.now());
        receiveTransaction.setValue(BigDecimal.valueOf(100));
        receiveTransaction.setTransactionType(TransactionType.TRANSFER);
        receiveTransaction.setFromIdAccount(senderAccount.getId());
        receiveTransaction.setToIdAccount(beneficiaryAccount.getId());
        beneficiaryAccount.getTransactions().add(receiveTransaction);
        beneficiaryAccount.setBalance(senderAccount.getBalance().add(BigDecimal.valueOf(100)));

        userService.realizeTransferTransaction(BigDecimal.valueOf(100),senderAccount,beneficiaryAccount);

        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository,atLeast(2)).save(transactionArgumentCaptor.capture());
        List<Transaction> passedTransactions = transactionArgumentCaptor.getAllValues();
        assertEquals(passedTransactions.get(0),sendTransaction);
        assertEquals(passedTransactions.get(1),receiveTransaction);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository,atLeast(2)).save(accountArgumentCaptor.capture());
        List<Account> passedAccounts = accountArgumentCaptor.getAllValues();
        assertEquals(passedAccounts.get(0),senderAccount);
        assertEquals(passedAccounts.get(1),beneficiaryAccount);

    }

    @Test
    void getBankStatement() {
    }

    @Test
    void createTransaction() {
        transaction = new Transaction();
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setValue(BigDecimal.valueOf(100));
        transaction.setTransactionType(TransactionType.DEPOSIT);

        userService.createTransaction(BigDecimal.valueOf(100),TransactionType.DEPOSIT);

        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionArgumentCaptor.capture());
        Transaction capturedTransaction = transactionArgumentCaptor.getValue();
        assertEquals(capturedTransaction,transaction);
    }

    @Test
    void shotDownUserBankAccount() {
    }
}