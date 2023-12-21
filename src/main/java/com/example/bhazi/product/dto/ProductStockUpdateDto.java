package com.example.bhazi.product.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ProductStockUpdateDto {
    @NotNull
    private boolean outOfStock;
}
