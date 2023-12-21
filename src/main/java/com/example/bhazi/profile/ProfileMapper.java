package com.example.bhazi.profile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.example.bhazi.finance.WalletMapper;
import com.example.bhazi.finance.domain.model.Wallet;
import com.example.bhazi.order.domain.OrderService;
import com.example.bhazi.order.domain.model.Order;
import com.example.bhazi.profile.domain.model.Address;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.profile.dto.ProfileCreateDto;
import com.example.bhazi.profile.dto.ProfileResponseDto;
import com.example.bhazi.profile.dto.ProfileUpdateDto;
import com.example.bhazi.referral.ReferralMapper;
import com.example.bhazi.referral.domain.model.Referral;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ProfileMapper {
    private final WalletMapper walletMapper;
    private final ReferralMapper referralMapper;
    private final AddressMapper addressMapper;
    private final OrderService orderService;

    public Profile mapToDomain(ProfileCreateDto profileCreateDto) {
        Profile profile = new Profile();
        profile.setFirstName(profileCreateDto.getFirstName());
        profile.setLastName(profileCreateDto.getLastName());
        profile.setEmailId(profileCreateDto.getEmailId());
        profile.setPhoneNumber(profileCreateDto.getPhoneNumber());
        profile.setUserId(profileCreateDto.getUserId());

        return profile;
    }

    public Profile mapToDomain(ProfileUpdateDto profileUpdateDto, Profile profile) {
        profile.setFirstName(profileUpdateDto.getFirstName());
        profile.setLastName(profileUpdateDto.getLastName());
        profile.setEmailId(profileUpdateDto.getEmailId());
        return profile;
    }

    public ProfileResponseDto mapToDto(Profile profile) {
        Wallet wallet = profile.getWallet();
        Referral referral = profile.getReferral();
        
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Optional<Order> order = orderService
                .getByProfileId(profile.getId(), pageable).stream()
                .filter(it -> it.getAddress().isActive())
                .findFirst();
        Address address = order.isPresent() ? order.get().getAddress() : null;

        return ProfileResponseDto.builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .emailId(profile.getEmailId())
                .phoneNumber(profile.getPhoneNumber())
                .firebaseToken(profile.getFirebaseToken())
                .walletId(wallet.getId())
                .walletBalance(wallet.getBalance())
                .referralId(referral.getId())
                .referralBalance(referral.getBalance())
                .referralCode(referral.getReferralCode())
                .userId(profile.getUserId())
                .wallet(walletMapper.mapToDto(wallet))
                .referral(referralMapper.mapToBasicDto(referral))
                .address(addressMapper.mapToDto(address))
                .build();
    }

    public List<ProfileResponseDto> mapToDtos(List<Profile> profiles) {
        return profiles.stream()
                .map(profile -> mapToDto(profile))
                .collect(Collectors.toList());
    }

    public ProfileResponseDto mapToBasicProfileDto(Profile profile) {
        return ProfileResponseDto.builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phoneNumber(profile.getPhoneNumber())
                .build();
    }
}
