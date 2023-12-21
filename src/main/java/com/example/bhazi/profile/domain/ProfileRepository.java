package com.example.bhazi.profile.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.profile.domain.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUserId(String userId);
    Optional<Profile> findByPhoneNumber(String phoneNumber);
}
