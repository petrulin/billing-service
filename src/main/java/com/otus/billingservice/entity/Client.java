package com.otus.billingservice.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "client", schema = "billing_service", catalog = "postgres")
@NoArgsConstructor
public class Client {

    private static final BigDecimal AMOUNT_DEFAULT_VALUE = BigDecimal.ZERO;
    private static final BigDecimal DISCOUNT_DEFAULT_VALUE = BigDecimal.ZERO;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "balance", nullable = false, precision = 2)
    private BigDecimal balance = AMOUNT_DEFAULT_VALUE;

    @Column(name = "currency", nullable = false)
    private Integer currency;

    @Column(name = "user_name", nullable = false)
    private String userName;

    public Client(String userName, Integer currency) {
        this.created = LocalDateTime.now();
        this.currency = currency;
        this.userName = userName;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = Objects.requireNonNullElse(balance, AMOUNT_DEFAULT_VALUE);
    }

}
