package com.restapi.atm.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Component
@Entity
@Table(name = "bankUser")
public class BankUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String password;
}