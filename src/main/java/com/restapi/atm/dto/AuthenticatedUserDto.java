package com.restapi.atm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticatedUserDto {
    String userName;
    String password;
}
