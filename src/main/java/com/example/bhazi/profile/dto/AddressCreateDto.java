package com.example.bhazi.profile.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressCreateDto {
    @NotBlank
    @Size(max = 255)
    private String completeAddress;
    
    // @NotBlank
    @Size(max = 20)
    String floor, tag;
    
    // @NotBlank
    @Size(max = 50)
    String instruction;
    
    String deliveryTimePref;

    @NotNull
    private BigDecimal lattitude, longitude;

    @NotNull
    private int profileId;
}
