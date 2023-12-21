package com.example.bhazi.finance.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bhazi.finance.domain.model.Transaction;

@Repository
public interface TransactionRespository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByProfileId(int profileId, Pageable pageable);
}
