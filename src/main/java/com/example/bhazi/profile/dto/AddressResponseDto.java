package com.example.bhazi.profile.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = Include.NON_DEFAULT)
public class AddressResponseDto {
    private long id;

    @JsonInclude(value = Include.ALWAYS)
    private String floor;
    
    private String completeAddress, instruction, deliveryTimePref, tag;
    private double distanceFromShop;
    private BigDecimal lattitude, longitude;
}
