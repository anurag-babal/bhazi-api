package com.example.bhazi.referral.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.bhazi.profile.domain.model.Profile;
import com.google.firebase.database.annotations.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "referral")
@Data
@NoArgsConstructor
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @NotBlank
    @Size(min = 8, max = 8)
    @Column(name = "referral_code", length = 8, unique = true, nullable = false)
    String referralCode;

    @NotNull
    @Column(name = "verified", nullable = false)
    boolean verified;

    @NotNull
    @Column(name = "balance", nullable = false)
    float balance;

    @NotNull
    @Column(name = "income", nullable = false)
    float income;
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Referral parent;
}
