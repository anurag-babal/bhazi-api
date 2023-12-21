package com.example.bhazi.referral.dto;

import javax.validation.constraints.Size;

import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReferralTransactionCreateDto {
    @NotNull
    private float amount;

    @NotNull
    private boolean status;

    @Size(max = 100)
    private String description;

    @NotNull
    private int referralId;
}
