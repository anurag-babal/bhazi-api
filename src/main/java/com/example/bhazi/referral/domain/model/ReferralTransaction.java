package com.example.bhazi.referral.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.example.bhazi.core.model.AuditModel;
import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "referral_transaction")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ReferralTransaction extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private float amount;

    @NotNull
    @Column(name = "status", nullable = false)
    private boolean status;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referral_id", nullable = false)
    private Referral referral;
}
