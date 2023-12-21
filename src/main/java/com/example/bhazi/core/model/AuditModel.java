package com.example.bhazi.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class AuditModel implements Serializable {

    @Column(name = "created_at", columnDefinition = "DATETIME(3)", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME(3)", nullable = false)
    @LastModifiedDate
    private Instant updatedAt;
}
