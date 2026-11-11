package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.model.User;
import kh.edu.istad.iam.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;

    @Transactional
    public UserResult execute(UUID userUuid, UpdateUserProfileCommand command) {
        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "uuid", userUuid));

        if (command.givenName() != null) user.setGivenName(command.givenName());
        if (command.familyName() != null) user.setFamilyName(command.familyName());
        if (command.middleName() != null) user.setMiddleName(command.middleName());
        if (command.nickname() != null) user.setNickname(command.nickname());
        if (command.gender() != null) user.setGender(command.gender());
        if (command.birthdate() != null) user.setBirthdate(command.birthdate());
        if (command.phoneNumber() != null) user.setPhoneNumber(command.phoneNumber());
        if (command.pictureUrl() != null) user.setPictureUrl(command.pictureUrl());
        if (command.profileUrl() != null) user.setProfileUrl(command.profileUrl());
        if (command.websiteUrl() != null) user.setWebsiteUrl(command.websiteUrl());
        if (command.zoneinfo() != null) user.setZoneinfo(command.zoneinfo());
        if (command.locale() != null) user.setLocale(command.locale());

        User saved = userRepository.save(user);

        return new UserResult(
                saved.getUuid(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getEmailVerified(),
                saved.getGivenName(),
                saved.getFamilyName(),
                saved.getGender(),
                saved.getBirthdate(),
                saved.getPhoneNumber(),
                saved.getPhoneNumberVerified(),
                saved.getPictureUrl(),
                saved.getEnabled()
        );
    }
}

