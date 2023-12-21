package com.example.bhazi.referral.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bhazi.referral.domain.model.Referral;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Integer> {
    Optional<Referral> findByReferralCode(String referralCode);
    Optional<Referral> findByProfileId(Integer profileId);
    // List<Referral> findAllByParentProfileId(Integer profileId);
    List<Referral> findAllByParentId(Integer parentId);
}
