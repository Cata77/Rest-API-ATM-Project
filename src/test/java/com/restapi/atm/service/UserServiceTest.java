package com.restapi.atm.service;

import com.restapi.atm.dto.AuthenticatedUserDto;
import com.restapi.atm.exception.UserNotFoundException;
import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.repository.AccountRepository;
import com.restapi.atm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Spy
    private UserRepository userRepository;
    @Spy
    private AccountRepository accountRepository;
    private AuthenticatedUserDto userDto;
    private BankUser bankUser1;

    @BeforeEach
    void setUp() {
        userDto = new AuthenticatedUserDto();
        userDto.setUserName("User 1");
        userDto.setPassword("testPass");

        bankUser1 = new BankUser();
        bankUser1.setUserName("User 1");
        bankUser1.setPassword("testPass");

        BankUser bankUser2 = new BankUser();
        bankUser2.setUserName("User 3");
        bankUser2.setPassword("testPass");
        userRepository.saveAll(List.of(bankUser1,bankUser2));
    }

    @Test
    void registerUser() {
        BankUser bankUser = new BankUser();
        bankUser.setUserName("Test User");
        bankUser.setPassword("testPass");

        Account account = new Account();
        account.setBankUser(bankUser);

        when(userRepository.findBankUserByUserName(userDto.getUserName())).thenReturn(Optional.of(bankUser));

        userService.registerUser(userDto);

        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account capturedAccount = accountArgumentCaptor.getValue();
        assertEquals(capturedAccount,account);

        ArgumentCaptor<BankUser> bankUserArgumentCaptor = ArgumentCaptor.forClass(BankUser.class);
        verify(userRepository).save(bankUserArgumentCaptor.capture());
        BankUser capturedBankUser = bankUserArgumentCaptor.getValue();
        assertEquals(capturedBankUser,bankUser);
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
    }

    @Test
    void createDepositTransaction() {
    }

    @Test
    void createWithdrawTransaction() {
    }

    @Test
    void createTransferTransaction() {
    }

    @Test
    void realizeTransferTransaction() {
    }

    @Test
    void getBankStatement() {
    }

    @Test
    void createTransaction() {
    }

    @Test
    void shotDownUserBankAccount() {
    }
}