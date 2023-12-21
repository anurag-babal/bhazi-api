package com.example.bhazi.profile.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileCreateDto {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 20)
    String firstName;

    String lastName;

    @Email(message = "Email not valid")
    @Size(max = 50)
    String emailId;

    @NotBlank(message = "Phone number is mandatory")
    @Size(max = 12)
    String phoneNumber;

    @Size(max = 8)
    String referralCode;

    @NotBlank(message = "UserId can't be blank")
    @Size(max = 64)
    String userId;
}
