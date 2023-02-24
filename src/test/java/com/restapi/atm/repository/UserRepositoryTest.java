package com.restapi.atm.repository;

import com.restapi.atm.model.BankUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;
    private BankUser user1;
    private BankUser user2;

    @BeforeEach
    void setUp() {
        user1 = new BankUser();
        user1.setUserName("Test User 1");
        user1.setPassword("testPass");
        user2 = new BankUser();
        user2.setUserName("Test User 2");
        user2.setPassword("pass123");
        userRepository.saveAll(Arrays.asList(user1, user2));
    }

    @Test
    void findBankUserByUserName() {
        Optional<BankUser> result = userRepository.findBankUserByUserName("Test User 1");
        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
    }

    @Test
    void findBankUserByUserNameAndPassword() {
        Optional<BankUser> result = userRepository.findBankUserByUserNameAndPassword("Test User 1", "testPass");
        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
    }

    @Test
    void findBankUserById() {
        Optional<BankUser> result = userRepository.findBankUserById(2);
        assertTrue(result.isPresent());
        assertEquals(user2, result.get());
    }
}