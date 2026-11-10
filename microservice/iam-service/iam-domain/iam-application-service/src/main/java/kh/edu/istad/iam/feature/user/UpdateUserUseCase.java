package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use case: Update user profile by UUID.
 */
@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserJpaRepository userJpaRepository;

    @Transactional
    public UserResponse execute(String uuid, UpdateUserRequest request) {
        User user = userJpaRepository.findByUuid(UUID.fromString(uuid))
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", uuid));

        if (request.givenName() != null) user.setGivenName(request.givenName());
        if (request.familyName() != null) user.setFamilyName(request.familyName());
        if (request.middleName() != null) user.setMiddleName(request.middleName());
        if (request.nickname() != null) user.setNickname(request.nickname());
        if (request.gender() != null) user.setGender(request.gender());
        if (request.birthdate() != null) user.setBirthdate(request.birthdate());
        if (request.phoneNumber() != null) user.setPhoneNumber(request.phoneNumber());
        if (request.pictureUrl() != null) user.setPictureUrl(request.pictureUrl());
        if (request.profileUrl() != null) user.setProfileUrl(request.profileUrl());
        if (request.websiteUrl() != null) user.setWebsiteUrl(request.websiteUrl());
        if (request.zoneinfo() != null) user.setZoneinfo(request.zoneinfo());
        if (request.locale() != null) user.setLocale(request.locale());

        user = userJpaRepository.save(user);
        return CreateUserUseCase.toResponse(user);
    }
}
