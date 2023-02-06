package com.restapi.atm.repository;

import com.restapi.atm.model.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<BankUser, Integer> {
    Optional<BankUser> findBankUserByUserName(String username);
    Optional<BankUser> findBankUserByUserNameAndPassword(String username, String password);
    Optional<BankUser> findBankUserById(Integer id);
}
