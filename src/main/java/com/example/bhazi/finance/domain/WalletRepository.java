package com.example.bhazi.finance.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.finance.domain.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByProfileId(Integer profileId);    
}
