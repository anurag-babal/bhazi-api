package com.example.bhazi.product.dto;

import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductPriceUpdateDto {
    @NotNull
    private int price;
}
