package com.restapi.atm.repository;

import com.restapi.atm.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByBankUserId(Integer id);

    @Query(value = "SELECT * FROM account WHERE balance = (SELECT MAX(balance) FROM account)",nativeQuery = true)
    Account findUserWithHighestBalance();
}
