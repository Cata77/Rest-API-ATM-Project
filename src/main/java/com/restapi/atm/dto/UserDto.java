package com.restapi.atm.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

    private Integer id;
    private String userName;
    private String password;
}
