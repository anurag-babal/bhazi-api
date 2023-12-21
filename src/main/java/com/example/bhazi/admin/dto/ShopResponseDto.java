package com.example.bhazi.admin.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = Include.NON_DEFAULT)
public class ShopResponseDto {
    private Byte id;
    private String tag;
    private String address;
    private BigDecimal lattitude;
    private BigDecimal longitude;
}
