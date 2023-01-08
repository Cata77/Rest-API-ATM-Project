package com.restapi.atm.controller;

import com.restapi.atm.dto.BankUserDto;
import com.restapi.atm.model.BankUser;
import com.restapi.atm.service.UserServiceImpl;
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
    private final UserServiceImpl userService;

    public AuthenticationController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<BankUserDto> registerUser(@RequestBody BankUser bankUser) {
        BankUser registeredBankUser = userService.registerUser(bankUser);

        BankUserDto bankUserDto = modelMapper.map(registeredBankUser,BankUserDto.class);

        return new ResponseEntity<>(bankUserDto,HttpStatus.CREATED);
    }
}
