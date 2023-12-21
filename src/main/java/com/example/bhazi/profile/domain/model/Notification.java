package com.example.bhazi.profile.domain.model;

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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Notification extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 64)
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Size(max = 255)
    @Column(name = "body", nullable = false)
    private String body;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 20, nullable = false)
    private NotificationCategory category;

    @NotNull
    @Column(name = "already_read", nullable = false)
    private Boolean read;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}
