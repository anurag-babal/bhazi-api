package com.example.bhazi.profile.domain;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.admin.domain.FirebaseMessagingService;
import com.example.bhazi.admin.domain.model.BhaziMessage;
import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.finance.domain.WalletService;
import com.example.bhazi.finance.domain.model.Wallet;
import com.example.bhazi.profile.NotificationMapper;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.referral.domain.ReferralService;
import com.example.bhazi.referral.domain.model.Referral;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final WalletService walletService;
    private final ReferralService referralService;
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final FirebaseMessagingService firebaseMessagingService;

    @Transactional
    public Profile save(Profile profile, String referralCode) {
        Profile savedProfile;
        Wallet wallet = walletService.createWallet();
        Referral referral = referralService.createReferral(referralCode);
        wallet.setProfile(profile);
        referral.setProfile(profile);
        profile.setWallet(wallet);
        profile.setReferral(referral);
        savedProfile = profileRepository.save(profile);
        if (referral.getParent() != null) {
            Profile parent = referral.getParent().getProfile();
            String firebaseToken = parent.getFirebaseToken();
            if (firebaseToken != null) {
                sendNotification(
                    messageForReferralJoin(parent.getFirstName(), savedProfile.getFirstName()), 
                    firebaseToken, 
                    referral.getParent().getProfile()
                );
            }
        }
        return savedProfile;
    }

    public BhaziMessage messageForReferralJoin(String parent, String child) {
        return BhaziMessage.builder()
            .title("Congratulation " + parent)
            .body("A new user " + child +" has been added as your referral child")
            .category("Join")
            .build();
    }
    
    public BhaziMessage messageForNewUser(String name) {
        return BhaziMessage.builder()
            .title("Welcome " + name)
            .body("Thanks for joining")
            .category("Join")
            .build();
    }

    public void sendNotification(BhaziMessage message, String token, Profile profile) {
        String response = null;
        try {
            response = firebaseMessagingService.sendNotification(
                message,
                token
            );
        } catch (FirebaseMessagingException e) {
            log.info("Notification_Firebase_Profile: {}" + e.getLocalizedMessage());
        }
        if (response != null) {
            notificationService.save(notificationMapper.mapToDomain(message, profile));
        }
    }

    @Transactional
    public Profile update(Profile profile) {
        return profileRepository.save(profile);
    }

    @Transactional
    public Profile updateFirebaseToken(int id, String firebaseToken) {
        Profile profile = getById(id);
        if (profile.getFirebaseToken() == null) {
            sendNotification(
                messageForNewUser(profile.getFirstName()), 
                firebaseToken, 
                profile
            );
        }
        profile.setFirebaseToken(firebaseToken);
        return profileRepository.save(profile);
    }

    public boolean delete(int id) {
        Profile profile = getById(id);
        profileRepository.delete(profile);
        return true;
    }

    public List<Profile> getAll(Pageable pageable) {
        return profileRepository.findAll(pageable).getContent();
    }

    public Profile getByUserId(String userId) {
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(
            () -> new EntityNotFoundException("Profile not present with userid = " + userId)
        );
        return profile;
    }

    public Profile getById(int id) {
        Profile profile = profileRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Profile not present with id = " + id)
        );
        return profile;
    }

    public Profile getByPhoneNumber(String phoneNumber) {
        Profile profile = profileRepository.findByPhoneNumber(phoneNumber).orElseThrow(
            () -> new EntityNotFoundException("Profile not present with phone number = " + phoneNumber)
        );
        return profile;
    }

    public Wallet getWalletByProfileId(int profileId) {
        return getById(profileId).getWallet();
    }

    public Referral getReferralByProfileId(int profileId) {
        return getById(profileId).getReferral();
    }
}
