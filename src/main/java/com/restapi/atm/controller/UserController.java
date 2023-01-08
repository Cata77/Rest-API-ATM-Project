package com.restapi.atm.controller;

import com.restapi.atm.dto.AccountDto;
import com.restapi.atm.model.Account;
import com.restapi.atm.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private ModelMapper modelMapper;
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<AccountDto> getUserDetails(@PathVariable String id) {
        Account account = userService.getUserDetails(Integer.parseInt(id));

        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }


}
