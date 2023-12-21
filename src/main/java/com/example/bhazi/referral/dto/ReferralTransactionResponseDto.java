package com.example.bhazi.referral.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReferralTransactionResponseDto {
    private long id;
    private float amount;
    private boolean status;
    private String description;
    private String createdAt;
}
