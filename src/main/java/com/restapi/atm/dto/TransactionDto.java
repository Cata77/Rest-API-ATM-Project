package com.restapi.atm.dto;

import com.restapi.atm.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {

    private Integer id;
    private LocalDateTime timestamp;
    private BigDecimal value;
    private TransactionType transactionType;
    private Integer fromIdAccount;
    private Integer toIdAccount;
}
