package com.example.bhazi.finance.dto;

import com.example.bhazi.finance.domain.model.PaymentMode;

import lombok.Data;

@Data
public class TransactionCreateDto {
    private float amount;
    private String paymentMode;
    private boolean status;
    private String description;
    private int profileId;
}
