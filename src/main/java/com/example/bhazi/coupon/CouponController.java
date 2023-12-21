package com.example.bhazi.coupon;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.coupon.domain.CouponService;
import com.example.bhazi.coupon.domain.model.Coupon;
import com.example.bhazi.coupon.domain.model.CouponApplied;
import com.example.bhazi.coupon.dto.CouponApplyRequestDto;
import com.example.bhazi.coupon.dto.CouponResponseDto;
import com.example.bhazi.profile.domain.ProfileService;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    private final ProfileService profileService;
    private final CouponMapper couponMapper;

    @GetMapping("/{code}")
    public ResponseDto getCoupon(@PathVariable(name = "code") String couponCode) {
        Coupon coupon = couponService.getById(couponCode);
        CouponResponseDto couponResponseDto = couponMapper.mapToCouponResponseDto(coupon);
        return ResponseBuilder.createResponse(couponResponseDto);
    }

    @GetMapping("")
    public ResponseDto getCoupons(
        @RequestParam(name = "profileId") Optional<Integer> profileId,
        Pageable pageable
    ) {
        List<Coupon> coupons;
        if (profileId.isPresent()) {
            coupons = couponService.getAllValidForProfile(profileId.get(), pageable);
        } else {
            coupons = couponService.getAllValid(pageable).toList();
        }

        return ResponseBuilder.createListResponse(
            coupons.size(), 
            couponMapper.mapToCouponResponseDtos(coupons)
        );
    }

    @GetMapping("/eligible")
    public ResponseDto checkEligible(
        @RequestParam(name = "profileId") int profileId,
        @RequestParam(name = "code") String couponCode
    ) {
        boolean eligible = false;
        if (couponService.isProfileEligibleForCoupon(profileId, couponCode)) {
            eligible = true;
        }
        return ResponseBuilder.createResponse(eligible);
    }

    @PostMapping("")
    public ResponseDto applyCoupon(
        @Valid @RequestBody CouponApplyRequestDto couponApplyRequestDto
    ) {
        Profile profile = profileService.getById(couponApplyRequestDto.getProfileId());
        Coupon coupon = couponService.getById(couponApplyRequestDto.getCouponCode());
        CouponApplied couponApplied = couponMapper
            .mapToDomain(couponApplyRequestDto, profile, coupon);
        couponService.applyCoupon(couponApplied);
        return ResponseBuilder.createResponse("Coupon applied successfully");
    }
}
