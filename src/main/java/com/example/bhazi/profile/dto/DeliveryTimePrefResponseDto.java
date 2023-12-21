package com.example.bhazi.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryTimePrefResponseDto {
    private String key, value;
}
