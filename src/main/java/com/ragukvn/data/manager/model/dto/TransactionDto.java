package com.ragukvn.data.manager.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TransactionDto {

    @NotNull(message = "Transaction ID cannot be null")
    @Min(value = 1, message = "Transaction ID must be greater than or equal to 1")
    private Long transactionId;

    @NotBlank(message = "Account number cannot be blank or null")
    private String accountNumber;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount must be greater than or equal to zero")
    private BigDecimal trxAmount;

    @NotBlank(message = "Description cannot be blank or null")
    private String description;

    @NotNull(message = "Transaction date cannot be null")
    @PastOrPresent(message = "Transaction date must be in the past or present")
    private LocalDate trxDate;

    @NotNull(message = "Transaction time cannot be null")
    //@PastOrPresent(message = "Transaction time must be in the past or present")
    private LocalTime trxTime;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Customer ID cannot be null")
    @Min(value = 0, message = "Version must be greater than or equal to zero")
    private Long version;
}
