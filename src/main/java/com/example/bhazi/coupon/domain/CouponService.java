package com.example.bhazi.coupon.domain;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.core.exception.GlobalException;
import com.example.bhazi.coupon.domain.model.Coupon;
import com.example.bhazi.coupon.domain.model.CouponApplied;
import com.example.bhazi.profile.domain.ProfileService;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.util.DateUtil;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponAppliedRepository couponAppliedRepository;

    private final ProfileService profileService;

    @Transactional
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Transactional
    public Coupon update(String couponCode, Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public void delete(String couponCode) {
        Coupon coupon = getById(couponCode);
        couponRepository.delete(coupon);
    }

    public boolean applyCouponNew(String couponCode, int profileId, float discount) {
    	Coupon coupon = getById(couponCode);
    	Profile profile = profileService.getById(profileId); 
    	CouponApplied couponApplied = new CouponApplied();
    	couponApplied.setCoupon(coupon);
    	couponApplied.setProfile(profile);
    	couponApplied.setDiscountAmount(discount);
    	applyCoupon(couponApplied);
    	return true;
    }
    
    @Transactional
    public CouponApplied applyCoupon(CouponApplied couponApplied) {
        if (isProfileEligibleForCoupon(
            couponApplied.getProfile().getId(), 
            couponApplied.getCoupon().getCouponCode())
        ) {
            return couponAppliedRepository.save(couponApplied);
        }
        
        throw new GlobalException("Not eligible for this code");
    }

    public Page<Coupon> getAll(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    public Page<Coupon> getAllValid(Pageable pageable) {
        return couponRepository.findAllByValid(true, pageable);
    }

    public Coupon getById(String couponCode) {
        Coupon coupon = couponRepository.findById(couponCode).orElseThrow(
            () -> new EntityNotFoundException("Coupon not found with code = " + couponCode)
        );
        return coupon;
    }

    public List<Coupon> getAllValidForProfile(int profileId, Pageable pageable) {
        return getAllValid(pageable)
            .filter( coupon -> isProfileEligibleForCoupon(profileId, coupon.getCouponCode()))
            .toList();
    }

    public boolean isProfileEligibleForCoupon(int profileId, String couponCode) {
        boolean eligible = false;
        Coupon coupon = getById(couponCode);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("updatedAt").descending());
        List<CouponApplied> couponAppliedList = couponAppliedRepository
            .findAllByProfileIdAndCoupon(profileId, coupon, pageable)
            .stream()
            .filter(CouponApplied::isValid)
            .collect(Collectors.toList());

        switch (coupon.getCouponType()) {
            case SINGLE:
                if (couponAppliedList.isEmpty()) {
                    eligible = true;
                }
                break;
            case MULTIPLE:
                if (couponAppliedList.isEmpty()) {
                    eligible = true;
                } else {
                    Instant updatedAt = couponAppliedList.get(0).getUpdatedAt();
                    int daysDifference = DateUtil.differenceBetweenDays(Instant.now(), updatedAt);
                    if (daysDifference > 0) {
                        eligible = true;
                    }
                }
                break;
            default:
                throw new GlobalException("Something went wrong, Please try again");
        }
        return eligible;
    }
}
