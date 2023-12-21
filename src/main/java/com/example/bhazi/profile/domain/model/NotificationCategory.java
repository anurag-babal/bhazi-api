package com.example.bhazi.profile.domain.model;

import lombok.Getter;

@Getter
public enum NotificationCategory {
    SHOP("Shop"),
    JOIN("Join"),
    CREDIT("Credit");

    private String description;

    NotificationCategory(String description) {
        this.description = description;
    }
}
