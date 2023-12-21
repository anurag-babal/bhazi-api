package com.example.bhazi.admin.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    byte id;

    @Column(name = "tag", length = 20, nullable = false, unique = true)
    private String tag;

    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "lattitude", columnDefinition = "DECIMAL(10,8) NOT NULL")
    private BigDecimal lattitude;

    @Column(name = "longitude", columnDefinition = "DECIMAL(11,8) NOT NULL")
    private BigDecimal longitude;
}
