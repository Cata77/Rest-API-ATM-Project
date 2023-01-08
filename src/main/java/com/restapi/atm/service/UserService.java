package com.restapi.atm.service;

import com.restapi.atm.model.BankUser;

public interface UserService {
    BankUser registerUser(BankUser bankUser);
    BankUser loginUser(BankUser bankUser);
}
