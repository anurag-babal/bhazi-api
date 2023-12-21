package com.example.bhazi.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponseDto {
    private long id;
    private String title, body;
    private Boolean read;
    private String category, time;
    private int profileId;
}
