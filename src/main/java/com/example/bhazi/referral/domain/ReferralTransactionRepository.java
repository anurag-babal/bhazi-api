package com.example.bhazi.referral.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bhazi.referral.domain.model.ReferralTransaction;

@Repository
public interface ReferralTransactionRepository extends JpaRepository<ReferralTransaction, Long> {
    Page<ReferralTransaction> findByReferralId(Integer referralId, Pageable pageable);
}
