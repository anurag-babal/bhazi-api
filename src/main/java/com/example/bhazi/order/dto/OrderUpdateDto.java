package com.example.bhazi.order.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderUpdateDto {
    @NotBlank
    private String status;
}
