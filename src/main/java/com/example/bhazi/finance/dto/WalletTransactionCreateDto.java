package com.example.bhazi.finance.dto;

import javax.validation.constraints.NotBlank;

import com.google.firebase.database.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionCreateDto {
    @NotNull float amount;
    @NotNull boolean status;
    String description;
    @NotNull int walletId;
    @NotBlank String userId;
}
