package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;

    @Override
    public boolean existsByName(String name) {
        return permissionJpaRepository.existsByName(name);
    }

    @Override
    public kh.edu.istad.iam.domain.model.Permission save(kh.edu.istad.iam.domain.model.Permission permission) {
        Permission entity = toEntity(permission);
        Permission saved = permissionJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Permission> findById(Long id) {
        return permissionJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Permission> findByUuid(UUID uuid) {
        return permissionJpaRepository.findByUuid(uuid).map(this::toDomain);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Permission> findByName(String name) {
        return permissionJpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public List<kh.edu.istad.iam.domain.model.Permission> findAll(int page, int size) {
        return permissionJpaRepository.findAllByIsDeletedFalse(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return permissionJpaRepository.count();
    }

    private kh.edu.istad.iam.domain.model.Permission toDomain(Permission entity) {
        kh.edu.istad.iam.domain.model.Permission model = new kh.edu.istad.iam.domain.model.Permission();
        model.setId(entity.getId());
        model.setUuid(entity.getUuid());
        model.setName(entity.getName());
        model.setResource(entity.getResource());
        model.setAction(entity.getAction());
        model.setDescription(entity.getDescription());
        model.setIsSystem(entity.getIsSystem());
        model.setIsActive(entity.getIsActive());
        model.setIsDeleted(entity.getIsDeleted());
        model.setDeletedAt(entity.getDeletedAt());
        model.setDeletedBy(entity.getDeletedBy());
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setVersion(entity.getVersion());
        return model;
    }

    private Permission toEntity(kh.edu.istad.iam.domain.model.Permission model) {
        Permission entity = new Permission();
        entity.setId(model.getId());
        entity.setUuid(model.getUuid());
        entity.setName(model.getName());
        entity.setResource(model.getResource());
        entity.setAction(model.getAction());
        entity.setDescription(model.getDescription());
        entity.setIsSystem(model.getIsSystem());
        entity.setIsActive(model.getIsActive());
        entity.setIsDeleted(model.getIsDeleted());
        entity.setDeletedAt(model.getDeletedAt());
        entity.setDeletedBy(model.getDeletedBy());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setVersion(model.getVersion());
        return entity;
    }
}

