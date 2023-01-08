package com.restapi.atm.service;

import com.restapi.atm.model.Account;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.model.Transaction;

import java.math.BigDecimal;

public interface UserService {
    BankUser registerUser(BankUser bankUser);
    BankUser loginUser(BankUser bankUser);
    Account getUserDetails(Integer id);
    Transaction createDepositTransaction(Integer id, BigDecimal amount);
}
