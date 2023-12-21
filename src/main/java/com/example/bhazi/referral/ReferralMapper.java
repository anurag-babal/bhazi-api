package com.example.bhazi.referral;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.referral.domain.model.Referral;
import com.example.bhazi.referral.domain.model.ReferralTransaction;
import com.example.bhazi.referral.dto.ReferralChildResponseDto;
import com.example.bhazi.referral.dto.ReferralResponseDto;
import com.example.bhazi.referral.dto.ReferralTransactionCreateDto;
import com.example.bhazi.referral.dto.ReferralTransactionResponseDto;
import com.example.bhazi.referral.dto.ReferralWalletResponseDto;
import com.example.bhazi.util.DateUtil;

@Component
public class ReferralMapper {

    public ReferralTransaction mapToTransactionDomain(
        ReferralTransactionCreateDto createDto,
        Referral referral
    ) {
        ReferralTransaction referralTransaction = new ReferralTransaction();
        referralTransaction.setAmount(createDto.getAmount());
        referralTransaction.setStatus(createDto.isStatus());
        referralTransaction.setDescription(createDto.getDescription());
        referralTransaction.setReferral(referral);
        return referralTransaction;
    }

    public ReferralTransactionResponseDto mapToTransactionDto(
        ReferralTransaction referralTransaction
    ) {
        return ReferralTransactionResponseDto.builder()
                .id(referralTransaction.getId())
                .amount(referralTransaction.getAmount())
                .status(referralTransaction.isStatus())
                .description(referralTransaction.getDescription())
                .createdAt(
                    DateUtil.getTimestampString(referralTransaction.getCreatedAt())
                )
                .build();
    }

    public List<ReferralTransactionResponseDto> mapToTransactionDtos(
        List<ReferralTransaction> referralTransactions
    ) {
        return referralTransactions.stream()
                .map(referralTransaction -> mapToTransactionDto(referralTransaction))
                .collect(Collectors.toList());
    }

    public ReferralResponseDto mapToDto(Referral referral, List<Referral> children) {
        Profile profile = referral.getProfile();
        return ReferralResponseDto.builder()
                .id(referral.getId())
                .referralCode(referral.getReferralCode())
                .balance(referral.getBalance())
                .profileId(profile.getId())
                .referralChildList(mapToChildDtos(children))
                .build();
    }

    public ReferralResponseDto mapToBasicDto(Referral referral) {
        return ReferralResponseDto.builder()
                .id(referral.getId())
                .referralCode(referral.getReferralCode())
                .balance(referral.getBalance())
                .build();
    }

    public ReferralChildResponseDto mapToChildDto(Referral referral, Profile profile) {
        return ReferralChildResponseDto.builder()
                .id(referral.getId())
                .profileId(profile.getId())
                .income(referral.getIncome())
                .name(profile.getFirstName() + " " + profile.getLastName())
                .verified(referral.isVerified())
                .build();
    }

    public List<ReferralChildResponseDto> mapToChildDtos(
        List<Referral> referrals
    ) {
        return referrals.stream()
                .map(referral -> mapToChildDto(referral, referral.getProfile()))
                .collect(Collectors.toList());
    }

    // Deprecated
    public ReferralWalletResponseDto mapToDto(Referral referral) {
        return ReferralWalletResponseDto.builder()
                .id(referral.getId())
                .balance(referral.getBalance())
                .build();
    }
}
