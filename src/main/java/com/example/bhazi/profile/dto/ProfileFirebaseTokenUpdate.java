package com.example.bhazi.profile.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileFirebaseTokenUpdate {
    @NotBlank
    @Size(max = 12)
    String phoneNumber;

    @NotBlank
    @Size(max = 200)
    String firebaseToken;
}
