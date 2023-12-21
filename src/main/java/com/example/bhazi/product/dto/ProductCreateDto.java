package com.example.bhazi.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCreateDto {
    @NotNull
    private int price;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    // TODO: Make nameHindi not null
    @Size(max = 50)
    private String nameHindi = "";

    @Size(max = 200)
    private String imageUrl;

    @Size(max = 500)
    private String descriptionStorage;

    @NotBlank
    @Size(max = 1024)
    String description;

    @NotBlank
    @Size(max = 15)
    String soldByPiece, type, subType, baseQuantity;

    @NotNull
    private byte numberOfPieces;

    boolean outOfStock, prime;
}
