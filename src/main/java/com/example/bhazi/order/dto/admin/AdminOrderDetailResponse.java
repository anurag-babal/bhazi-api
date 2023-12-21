package com.example.bhazi.order.dto.admin;

import java.util.List;

import com.example.bhazi.profile.dto.AddressResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminOrderDetailResponse {
    private long id;
    private String status;
    private float amount, deliveryCharge, packagingCharge;
    private float walletBalance, couponBalance, referralWalletBalance;
    private String paymentMode, deliveryType, packagingType, deliveryTimePref;
    private AddressResponseDto addressNew;
    private byte shopId;
    private String shopName, customerName, mobileNumber;
    private List<AdminOrderItemDetailResponse> orderItems;

    // TODO: Deprecated
    private String address, timestamp;
}
