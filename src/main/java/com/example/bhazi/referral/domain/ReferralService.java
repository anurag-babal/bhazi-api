package com.example.bhazi.referral.domain;

import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.referral.domain.model.Referral;
import com.example.bhazi.referral.domain.model.ReferralTransaction;
import com.example.bhazi.util.RandomString;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReferralService {
    private final ReferralRepository referralRepository;
    private final ReferralTransactionRepository referralTransactionRepository;

    public Referral createReferral(String referralCode) {
        Referral parentReferral, childReferral;
        childReferral = new Referral();
        if (referralCode != null && referralCode.length() == 8) {
            parentReferral = referralRepository.findByReferralCode(referralCode).orElseThrow(
                () -> new RuntimeException("Referral Code not valid")
            );
            childReferral.setParent(parentReferral);
        } else {
            childReferral.setParent(null);
        }

        // Generate a random string and check it for uniqueness
        String random = new RandomString(8, new Random()).nextString();
        while(referralRepository.findByReferralCode(random).isPresent()) {
            random = new RandomString(8, new Random()).nextString();
        }
        childReferral.setReferralCode(random);
        childReferral.setBalance(0.0f);
        return childReferral;
    }

    public Referral getById(int id) {
        Referral referral = referralRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Referral not found with id = " + id)
        );
        return referral;
    }

    public Referral getByProfileId(int profileId) {
        Referral referral = referralRepository.findByProfileId(profileId).orElseThrow(
            () -> new EntityNotFoundException("Referral not found for profile id = " + profileId)
        );
        return referral;
    }

    @Transactional
    private Referral save(Referral referral) {
        return referralRepository.save(referral);
    }

    @Transactional
    public ReferralTransaction saveTransaction(ReferralTransaction referralTransaction) {
        ReferralTransaction savedReferralTransaction;
        Referral referral = referralTransaction.getReferral();
        float balance = referral.getBalance() + referralTransaction.getAmount();
        referral.setBalance(balance);
        referralTransaction.setReferral(referral);
        save(referral);
        savedReferralTransaction = referralTransactionRepository.save(referralTransaction);
        return savedReferralTransaction;
    }

    public void updateVerified(Referral referral) {
        if (!referral.isVerified()) {
            referral.setVerified(true);
            referralRepository.save(referral);
        }
    }

    @Transactional
    public void distributeMoneyShare(float amount, Referral referral) {
        int i = 0;
        Referral childReferral;
        Referral parentReferral;
        childReferral = referral;
        parentReferral = childReferral.getParent();
        while (parentReferral != null && i++ < 10) {
            String childName = childReferral.getProfile().getFirstName();
            float balance = Math.abs((amount * (5.0f / i)) / 100);
            if (balance <= 0) break;
            ReferralTransaction referralTransaction = new ReferralTransaction();
            referralTransaction.setAmount(balance);
            referralTransaction.setStatus(true);
            referralTransaction.setDescription("Credited by " + childName);
            referralTransaction.setReferral(parentReferral);

            childReferral.setIncome(childReferral.getIncome() + balance);
            save(childReferral);
            saveTransaction(referralTransaction);
            childReferral = parentReferral;
            parentReferral = parentReferral.getParent();
        }
    }

    public ReferralTransaction getTransactionById(long id) {
        ReferralTransaction referralTransaction = referralTransactionRepository.findById(id)
                .orElseThrow( () -> 
                    new EntityNotFoundException("Transaction not found with id = " +id)
                );
        return referralTransaction;
    }

    public Page<ReferralTransaction> getTransactionsByReferralId(
        int referralId, Pageable pageable
    ) {
        return referralTransactionRepository.findByReferralId(referralId, pageable);
    }

    public List<Referral> getAllChildren(int profileId) {
        // return referralRepository.findAllByParentProfileId(profileId);
        return referralRepository.findAllByParentId(profileId);
    }
    
}
