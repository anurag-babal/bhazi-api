package com.example.bhazi.finance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletResponseDto {
    int id;
    float balance;
    int profileId;
}
