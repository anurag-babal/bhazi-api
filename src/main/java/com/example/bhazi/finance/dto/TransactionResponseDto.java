package com.example.bhazi.finance.dto;

import com.example.bhazi.finance.domain.model.PaymentMode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionResponseDto {
    private long id;
    private float amount;
    private String paymentMode;
    private boolean status;
    private String description;
    private String creationTimestamp;
    private String name, phoneNumber;
}
