package com.example.bhazi.referral.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = Include.NON_DEFAULT)
public class ReferralResponseDto {
    private int id;
    private String referralCode;
    @JsonInclude(value = Include.ALWAYS)
    private float balance;
    private int profileId;
    private List<ReferralChildResponseDto> referralChildList;
}
