package com.example.bhazi.finance.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.bhazi.core.model.AuditModel;
import com.example.bhazi.profile.domain.model.Profile;
import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Transaction extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @NotNull
    @Column(name = "amount", nullable = false)
    private float amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", length = 20, nullable = false)
    private PaymentMode paymentMode;

    @NotNull
    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Profile profile;
}
