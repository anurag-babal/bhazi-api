package com.example.bhazi.profile.dto;

import com.example.bhazi.finance.dto.WalletResponseDto;
import com.example.bhazi.referral.dto.ReferralResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = Include.NON_DEFAULT)
public class ProfileResponseDto {
    private int id;
    private String firstName, lastName, emailId, phoneNumber, firebaseToken;
    private WalletResponseDto wallet;
    private ReferralResponseDto referral;
    @JsonInclude(value = Include.ALWAYS)
    private AddressResponseDto address;

    // TODO: Deprecated
    private int walletId;
    private float walletBalance;
    private int referralId;
    private float referralBalance;
    private String referralCode;
    private String userId;
}
