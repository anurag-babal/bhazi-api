package com.example.bhazi.referral.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReferralWalletResponseDto {
    int id;
    float balance;
    int referralId;
}
