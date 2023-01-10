package com.restapi.atm.repository;

import com.restapi.atm.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT * FROM transaction WHERE account_id= :id AND timestamp BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Transaction> getTransactionsBetweenDates(@Param("id") Integer id,
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
