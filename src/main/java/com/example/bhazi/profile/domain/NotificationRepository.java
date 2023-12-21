package com.example.bhazi.profile.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.profile.domain.model.Notification;
import com.example.bhazi.profile.domain.model.Profile;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByProfile(Profile profile, Pageable pageable);
}
