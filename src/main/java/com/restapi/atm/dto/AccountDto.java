package com.restapi.atm.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AccountDto {

    private Integer id;
    private BigDecimal balance;
    private List<BasicTransactionDto> transactions;
}
