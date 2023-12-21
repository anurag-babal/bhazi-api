package com.example.bhazi.profile.domain.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.bhazi.core.model.AuditModel;
import com.example.bhazi.order.domain.model.DeliveryTimePref;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity()
@Table(name = "address")
@Data
@EqualsAndHashCode(callSuper = false)
public class Address extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "full_address", length = 255, nullable = false)
    private String fullAddress;

    // @NotNull
    @Size(max = 20)
    @Column(name = "floor", length = 20)
    private String floor;

    // @NotNull
    @Size(max = 50)
    @Column(name = "instruction", length = 50)
    private String instruction;

    // @NotNull
    @Size(max = 20)
    @Column(name = "tag", length = 20)
    private String tag;

    @Column(name = "lattitude", columnDefinition = "DECIMAL(10,8) NOT NULL")
    private BigDecimal lattitude;

    @Column(name = "longitude", columnDefinition = "DECIMAL(11,8) NOT NULL")
    private BigDecimal longitude;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_time_preference", length = 20, nullable = false)
    private DeliveryTimePref deliveryTimePref;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}
