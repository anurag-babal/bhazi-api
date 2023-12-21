package com.example.bhazi.order.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreateDto {
    @NotNull
    private float amount;
    @NotNull
    private float deliveryCharge;
    @NotNull
    private float packagingCharge;
    
    @NotNull
    private float couponBalance;
    @NotNull
    private float walletBalance;
    
    private float referralBalance;
    
    @NotNull
    private long addressId;

    @NotBlank
    private String paymentMode;

    private String packagingType;
    private String deliveryType;
    private String deliveryTimePref;
    private String phoneNumber;

    @NotNull
    private int profileId;

    @NotEmpty
    private List<OrderItemCreateDto> orderItems;

    // TODO: Deprecated
    private float referralWalletBalance;
    private byte deliveryId;
    private byte packagingId;
}
