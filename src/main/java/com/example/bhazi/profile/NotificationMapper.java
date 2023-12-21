package com.example.bhazi.profile;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.admin.domain.model.BhaziMessage;
import com.example.bhazi.profile.domain.model.Notification;
import com.example.bhazi.profile.domain.model.NotificationCategory;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.profile.dto.NotificationCreateDto;
import com.example.bhazi.profile.dto.NotificationResponseDto;
import com.example.bhazi.util.DateUtil;

@Component
public class NotificationMapper {
    public Notification mapToDomain(NotificationCreateDto notificationCreateDto, Profile profile) {
        Notification notification = new Notification();
        notification.setTitle(notificationCreateDto.getTitle());
        notification.setBody(notificationCreateDto.getBody());
        notification.setRead(false);
        notification.setCategory(
            NotificationCategory.valueOf(
                notificationCreateDto.getCategory().replace(" ", "_").toUpperCase()
            )
        );
        notification.setProfile(profile);
        return notification;
    }

    public Notification mapToDomain(BhaziMessage message, Profile profile) {
        Notification notification = new Notification();
        notification.setTitle(message.getTitle());
        notification.setBody(message.getBody());
        notification.setRead(false);
        notification.setCategory(NotificationCategory.valueOf(
            message.getCategory().replace(" ", "_").toUpperCase()
        ));
        notification.setProfile(profile);
        return notification;
    }

    public NotificationResponseDto mapToDto(Notification notification) {
        return NotificationResponseDto.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .body(notification.getBody())
            .read(notification.getRead())
            .category(notification.getCategory().getDescription())
            .time(DateUtil.getTimestampString(notification.getCreatedAt()))
            .profileId(notification.getProfile().getId())
            .build();
    }

    public List<NotificationResponseDto> mapToDtos(List<Notification> notifications) {
        return notifications.stream()
            .map(notification -> mapToDto(notification))
            .collect(Collectors.toList());
    }
}
