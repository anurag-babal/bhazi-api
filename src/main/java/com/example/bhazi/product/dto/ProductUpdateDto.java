package com.example.bhazi.product.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductUpdateDto {
    @NotBlank
    @Size(max = 30)
    private String name;
    
    @NotBlank
    @Size(max = 1024)
    String description;
    
    @NotBlank
    @Size(max = 15)
    String baseQuantity, soldByPiece, type, subType;

    @NotNull
    private byte numberOfPieces;
}
