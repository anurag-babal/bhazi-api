package com.example.bhazi.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChargeUpdateDto {
    private int id;
    private float transport, delivery, network, platform, api;
    private float hosting, operation, handling, profit, slippage;
    private String timestamp;
}
