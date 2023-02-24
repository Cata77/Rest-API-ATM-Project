package com.restapi.atm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@Entity
@Component
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    private BankUser bankUser;
    private BigDecimal balance;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private List<Transaction> transactions;

    public Account() {
        this.balance = BigDecimal.ZERO;
    }
}
