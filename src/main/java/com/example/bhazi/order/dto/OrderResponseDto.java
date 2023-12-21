package com.example.bhazi.order.dto;

import java.util.List;

import com.example.bhazi.admin.dto.ShopResponseDto;
import com.example.bhazi.profile.dto.AddressResponseDto;
import com.example.bhazi.profile.dto.ProfileResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDto {
    private long id;
    private String status;
    private float paybale, deliveryCharge, packagingCharge;
    private float couponBalance, walletBalance, referralBalance;
    private float amount;
    private String paymentMode, deliveryType, packagingType;
    private String deliveryTimePref;
    private String orderedAt;
    private ProfileResponseDto profile;
    private AddressResponseDto addressNew;
    private ShopResponseDto shop;
    private List<OrderItemResponseDto> orderItems;

    // TODO: Deprecated
    private int profileId;
    private long addressId;
    private byte packagingId, deliveryId, shopId;
    private float referralWalletBalance;
    private String customerName, customerNumber, address, shopAddress, timestamp;
}
