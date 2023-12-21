package com.example.bhazi.profile.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationCreateDto {
    @NotBlank
    @Size(max = 64)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String body;

    private Boolean read;

    @NotNull
    private String category;

    @NotNull
    private int profileId;
}
