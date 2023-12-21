package com.example.bhazi.referral.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReferralChildResponseDto {
    private int id;
    private float income;
    private String name;
    private boolean verified;
    private int profileId;
}
