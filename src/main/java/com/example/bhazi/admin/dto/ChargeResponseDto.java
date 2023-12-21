package com.example.bhazi.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChargeResponseDto {
    private int id;
    private float transport, delivery, network, platform, api, hosting, operation;
    private float handling, profit, slippage;
}
