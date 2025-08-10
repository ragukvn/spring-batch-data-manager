package com.ragukvn.data.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "trx_amount", nullable = false)
    private BigDecimal trxAmount;

    private String description;

    private LocalDate trxDate;

    private LocalTime trxTime;

    private Long customerId;

    @Version
    private Long version;

    // Creating a builder for Transaction entity excluding the transaction id which will be created automatically.
    @Builder
    public Transaction(String accountNumber, BigDecimal trxAmount, String description,
                       LocalDate trxDate, LocalTime trxTime, Long customerId) {
        this.accountNumber = accountNumber;
        this.trxAmount = trxAmount;
        this.description = description;
        this.trxDate = trxDate;
        this.trxTime = trxTime;
        this.customerId = customerId;
    }
}
