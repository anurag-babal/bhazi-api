package com.example.bhazi.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeCreateDto {
    private float transport, delivery, network, platform, api, hosting;
    private float operation, handling, profit, slippage;
    private String timestamp;
}
