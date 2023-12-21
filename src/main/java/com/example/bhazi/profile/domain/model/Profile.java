package com.example.bhazi.profile.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.bhazi.core.model.AuditModel;
import com.example.bhazi.finance.domain.model.Wallet;
import com.example.bhazi.referral.domain.model.Referral;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "profile")
@Data
@EqualsAndHashCode(callSuper = false)
public class Profile extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;

    @Size(max = 20)
    @Column(name = "last_name", length = 20)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 50)
    @Column(name = "email_id", length = 50, nullable = false)
    private String emailId;

    @NotBlank
    @Size(max = 12)
    @Column(name = "phone_number", length = 12, nullable = false, unique = true)
    private String phoneNumber;

    @Size(max = 200)
    @Column(name = "firebase_token", length = 200, unique = true)
    private String firebaseToken;

    @NotBlank
    @Size(max = 64)
    @Column(name = "user_id", length = 64, nullable = false, unique = true)
    private String userId;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
    private Wallet wallet;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Referral referral;
}
