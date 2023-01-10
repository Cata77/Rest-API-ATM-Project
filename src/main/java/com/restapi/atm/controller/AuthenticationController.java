package com.restapi.atm.controller;

import com.restapi.atm.dto.BankUserDto;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private ModelMapper modelMapper;
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<BankUserDto> registerUser(@RequestBody BankUser bankUser) {
        BankUser registeredBankUser = userService.registerUser(bankUser);

        BankUserDto bankUserDto = modelMapper.map(registeredBankUser,BankUserDto.class);

        return new ResponseEntity<>(bankUserDto,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BankUserDto> loginUser(@RequestBody BankUser bankUser) {
        BankUser loggedBankUser = userService.loginUser(bankUser);

        BankUserDto bankUserDto = modelMapper.map(loggedBankUser, BankUserDto.class);

        return new ResponseEntity<>(bankUserDto,HttpStatus.OK);
    }
}
