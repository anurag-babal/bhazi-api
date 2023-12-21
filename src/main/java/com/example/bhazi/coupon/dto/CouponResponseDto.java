package com.example.bhazi.coupon.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponResponseDto {
    private String couponCode;
    private int discount;
    private String description, expiryDate;
    private boolean valid;
}
