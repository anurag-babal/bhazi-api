package com.example.bhazi.profile.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.profile.domain.model.Notification;
import com.example.bhazi.profile.domain.model.Profile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional
    public boolean delete(long id) {
        Notification notification = getById(id);
        notificationRepository.delete(notification);
        return true;
    }

    public Page<Notification> getByProfile(Profile profile, Pageable pageable) {
        return notificationRepository.findByProfile(profile, pageable);
    }

    public Notification getById(long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Notification not found with id = " + id)
        );
        return notification;
    }
}
