package com.example.bhazi.coupon;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.coupon.domain.model.Coupon;
import com.example.bhazi.coupon.domain.model.CouponApplied;
import com.example.bhazi.coupon.dto.CouponApplyRequestDto;
import com.example.bhazi.coupon.dto.CouponResponseDto;
import com.example.bhazi.profile.domain.model.Profile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponMapper {

    public CouponApplied mapToDomain(
        CouponApplyRequestDto couponApplyRequestDto, 
        Profile profile, 
        Coupon coupon
    ) {
        CouponApplied couponApplied = new CouponApplied();
        couponApplied.setCoupon(coupon);
        couponApplied.setProfile(profile);
        couponApplied.setValid(true);
        couponApplied.setDiscountAmount(couponApplyRequestDto.getDiscountAmount());
        return couponApplied;
    }

    public CouponResponseDto mapToCouponResponseDto(Coupon coupon) {
        return CouponResponseDto.builder()
            .couponCode(coupon.getCouponCode())
            .discount(coupon.getDiscountPercent())
            .description(coupon.getDescription())
            .expiryDate(coupon.getExpiryDate().toString())
            .valid(coupon.isValid())
            .build();
    }

    public List<CouponResponseDto> mapToCouponResponseDtos(List<Coupon> coupons) {
        return coupons.stream()
            .map(coupon -> mapToCouponResponseDto(coupon))
            .collect(Collectors.toList());
    }
}
