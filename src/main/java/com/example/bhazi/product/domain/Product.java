package com.example.bhazi.product.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.bhazi.product.data.entity.SubProductEntity;

import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product implements Serializable {
    private static final long serialVersionUID = 45070573703248578L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Size(max = 30)
    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(max = 50)
    @Column(name = "name_hindi", length = 50, nullable = false, unique = true)
    private String nameHindi = "";  // TODO: Deprecated, make not null

    @Size(max = 200)
    @Column(name = "img_url", length = 200)
    private String imageUrl;

    @NotNull
    @Column(name = "base_price", nullable = false)
    private int basePrice;

    @NotNull
    @Column(name = "selling_price", nullable = false)
    private int sellingPrice;

    @NotNull
    @Column(name = "prime", nullable = false)
    private boolean prime = false;

    @NotNull
    @Size(max = 1024)
    @Column(name = "description", length = 1024, nullable = false)
    private String description;

    @Size(max = 500)
    @Column(name = "description_storage", length = 500)
    private String descriptionStorage;

    @NotNull
    @Size(max = 15)
    @Column(name = "type", length = 15, nullable = false)
    private String type;

    @NotNull
    @Size(max = 15)
    @Column(name = "sub_type", length = 15, nullable = false)
    private String subType;

    @NotNull
    @Size(max = 15)
    @Column(name = "sold_by_piece", length = 15, nullable = false)
    private String soldByPiece;
    
    @NotNull
    @Size(max = 30)
    @Column(name = "base_quantity", length = 15, nullable = false)
    private String baseQuantity;

    @NotNull
    @Column(name = "number_of_pieces", nullable = false)
    private byte numberOfPieces;

    @NotNull
    @Column(name = "out_of_stock", nullable = false)
    private boolean outOfStock = false;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Collection<SubProductEntity> subProducts;
}
