package com.example.bhazi.finance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletTransactionResponseDto {
    private long id;
    private int walletId;
    private float amount;
    private boolean status;
    private String description, timestamp;
}
