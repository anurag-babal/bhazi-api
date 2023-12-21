package com.example.bhazi.profile;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.profile.domain.ProfileService;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.profile.dto.ProfileCreateDto;
import com.example.bhazi.profile.dto.ProfileFirebaseTokenUpdate;
import com.example.bhazi.profile.dto.ProfileResponseDto;
import com.example.bhazi.profile.dto.ProfileUpdateDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @PostMapping("")
    public ResponseDto save(
        @Valid @RequestBody ProfileCreateDto profilecCreateDto
    ) {
        Profile profile = profileMapper.mapToDomain(profilecCreateDto);
        Profile savedProfile = profileService.save(profile, profilecCreateDto.getReferralCode());
        return ResponseBuilder.createResponse(profileMapper.mapToDto(savedProfile));
    }

    @PutMapping("/{id}")
    public ResponseDto update(
        @PathVariable(name = "id") int id, 
        @Valid @RequestBody ProfileUpdateDto profileUpdateDto
    ) {
        Profile profile = profileMapper.mapToDomain(
            profileUpdateDto, profileService.getById(id)
        );
        Profile updatedProfile = profileService.update(profile);
        return ResponseBuilder.createResponse(
            profileMapper.mapToDto(updatedProfile)
        );
    }

    @PutMapping("/token")
    public ResponseDto updateFirebaseToken(
        @Valid @RequestBody ProfileFirebaseTokenUpdate pTokenUpdate
    ) {
        Profile profile = profileService.getByPhoneNumber(pTokenUpdate.getPhoneNumber());
        int id = profile.getId();
        String firebaseToken = pTokenUpdate.getFirebaseToken();
        Profile updatedProfile = profileService.updateFirebaseToken(id, firebaseToken);
        return ResponseBuilder.createResponse(
            profileMapper.mapToDto(updatedProfile)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseDto delete(@PathVariable(name = "id") int id) {
        return ResponseBuilder.createDeleteResponse(profileService.delete(id));
    }
    
    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable(name = "id") int id) {
        Profile profile = profileService.getById(id);
        ProfileResponseDto responseDto = profileMapper.mapToDto(profile);
        return ResponseBuilder.createResponse(responseDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseDto getByUserId(@PathVariable(name = "userId") String userId) {
        Profile profile = profileService.getByUserId(userId);
        ProfileResponseDto responseDto = profileMapper.mapToDto(profile);
        return ResponseBuilder.createResponse(responseDto);
    }

    // TODO: Deprecated
    @GetMapping("")
    public ResponseDto getAll(
        @RequestParam(defaultValue = "0", name = "page") int page
    ) {
        Pageable pageable = PageRequest.of(page, 60, Sort.by("firstName"));
        List<Profile> profiles = profileService.getAll(pageable);
        List<ProfileResponseDto> responseDtos = profileMapper.mapToDtos(profiles);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }
}
