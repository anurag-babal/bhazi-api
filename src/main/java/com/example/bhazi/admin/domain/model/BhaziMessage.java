package com.example.bhazi.admin.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BhaziMessage {
    private String title, body, category, image;
}
