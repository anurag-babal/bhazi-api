package com.example.bhazi.profile.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ProfileUpdateDto {
    @NotBlank
    @Size(max = 20)
    String firstName;

    String lastName;

    @NotBlank
    @Email(message = "Email not valid")
    @Size(max = 50)
    String emailId;
}
