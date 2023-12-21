package com.example.bhazi.admin.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.bhazi.core.model.AuditModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "charge")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Charge extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private float transport;
    private float delivery;
    private float network;
    private float platform;
    private float api;
    private float hosting;
    private float operation;
    private float handling;
    private float profit;
    private float slippage;
}
