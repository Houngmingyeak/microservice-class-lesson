package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.UserEntity;
import kh.edu.istad.iam.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setUuid(entity.getUuid());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setEmailVerified(entity.getEmailVerified());
        user.setPasswordHash(entity.getPasswordHash());
        user.setGivenName(entity.getGivenName());
        user.setFamilyName(entity.getFamilyName());
        user.setMiddleName(entity.getMiddleName());
        user.setNickname(entity.getNickname());
        user.setGender(entity.getGender());
        user.setBirthdate(entity.getBirthdate());
        user.setProfileUrl(entity.getProfileUrl());
        user.setPictureUrl(entity.getPictureUrl());
        user.setWebsiteUrl(entity.getWebsiteUrl());
        user.setZoneinfo(entity.getZoneinfo());
        user.setLocale(entity.getLocale());
        user.setPhoneNumber(entity.getPhoneNumber());
        user.setPhoneNumberVerified(entity.getPhoneNumberVerified());
        user.setAddressStreetAddress(entity.getAddressStreetAddress());
        user.setAddressLocality(entity.getAddressLocality());
        user.setAddressRegion(entity.getAddressRegion());
        user.setAddressPostalCode(entity.getAddressPostalCode());
        user.setAddressCountry(entity.getAddressCountry());
        user.setEnabled(entity.getEnabled());
        user.setAccountNonExpired(entity.getAccountNonExpired());
        user.setAccountNonLocked(entity.getAccountNonLocked());
        user.setCredentialsNonExpired(entity.getCredentialsNonExpired());
        user.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        user.setLastLoginAt(entity.getLastLoginAt());
        user.setPasswordChangedAt(entity.getPasswordChangedAt());
        user.setIsDeleted(entity.getIsDeleted());
        user.setDeletedAt(entity.getDeletedAt());
        user.setDeletedBy(entity.getDeletedBy());
        user.setVersion(entity.getVersion());
        user.setCreatedAt(entity.getCreatedAt());
        user.setCreatedBy(entity.getCreatedBy());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setUpdatedBy(entity.getUpdatedBy());
        return user;
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUuid(user.getUuid());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setEmailVerified(user.getEmailVerified());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setGivenName(user.getGivenName());
        entity.setFamilyName(user.getFamilyName());
        entity.setMiddleName(user.getMiddleName());
        entity.setNickname(user.getNickname());
        entity.setGender(user.getGender());
        entity.setBirthdate(user.getBirthdate());
        entity.setProfileUrl(user.getProfileUrl());
        entity.setPictureUrl(user.getPictureUrl());
        entity.setWebsiteUrl(user.getWebsiteUrl());
        entity.setZoneinfo(user.getZoneinfo());
        entity.setLocale(user.getLocale());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setPhoneNumberVerified(user.getPhoneNumberVerified());
        entity.setAddressStreetAddress(user.getAddressStreetAddress());
        entity.setAddressLocality(user.getAddressLocality());
        entity.setAddressRegion(user.getAddressRegion());
        entity.setAddressPostalCode(user.getAddressPostalCode());
        entity.setAddressCountry(user.getAddressCountry());
        entity.setEnabled(user.getEnabled());
        entity.setAccountNonExpired(user.getAccountNonExpired());
        entity.setAccountNonLocked(user.getAccountNonLocked());
        entity.setCredentialsNonExpired(user.getCredentialsNonExpired());
        entity.setFailedLoginAttempts(user.getFailedLoginAttempts());
        entity.setLastLoginAt(user.getLastLoginAt());
        entity.setPasswordChangedAt(user.getPasswordChangedAt());
        entity.setIsDeleted(user.getIsDeleted());
        entity.setDeletedAt(user.getDeletedAt());
        entity.setDeletedBy(user.getDeletedBy());
        entity.setVersion(user.getVersion());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setCreatedBy(user.getCreatedBy());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setUpdatedBy(user.getUpdatedBy());
        return entity;
    }
}
