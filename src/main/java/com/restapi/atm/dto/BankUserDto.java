package com.restapi.atm.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankUserDto {
    private Integer id;
    private String userName;
    private String password;
}
