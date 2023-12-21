package com.example.bhazi.profile;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.profile.domain.NotificationService;
import com.example.bhazi.profile.domain.ProfileService;
import com.example.bhazi.profile.domain.model.Notification;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.profile.dto.NotificationCreateDto;
import com.example.bhazi.profile.dto.NotificationResponseDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final ProfileService profileService;

    @PostMapping("")
    public ResponseDto save(
        @Valid @RequestBody List<NotificationCreateDto> notificationCreateDtos
    ) {
        int profileId;
        List<Notification> savedNotifications = new ArrayList<>();
        for (NotificationCreateDto notificationCreateDto : notificationCreateDtos) {
            profileId = notificationCreateDto.getProfileId();
            Profile profile = profileService.getById(profileId);
            Notification savedNotification;
            Notification notification = notificationMapper
                .mapToDomain(notificationCreateDto, profile);
            savedNotification = notificationService.save(notification);
            savedNotifications.add(savedNotification);  
        }
        List<NotificationResponseDto> responseDtos = notificationMapper
            .mapToDtos(savedNotifications);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseDto getById(
        @PathVariable(name = "id") long id
    ) {
        Notification notification = notificationService.getById(id);
        NotificationResponseDto responseDto = notificationMapper.mapToDto(notification);
        return ResponseBuilder.createResponse(responseDto);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseDto getAllByProfileId(
        @PathVariable(name = "profileId") int profileId
    ) {
        Profile profile = profileService.getById(profileId);
        Pageable pageable = PageRequest.of(
            0, 20, Sort.by("id").descending()
        );
        List<Notification> notifications = notificationService
            .getByProfile(profile, pageable)
            .getContent();

        List<NotificationResponseDto> responseDtos = notificationMapper.mapToDtos(notifications);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }
}
