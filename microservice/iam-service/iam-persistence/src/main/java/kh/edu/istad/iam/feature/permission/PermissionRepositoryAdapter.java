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
    private final PermissionPersistenceMapper permissionPersistenceMapper;

    @Override
    public boolean existsByName(String name) {
        return permissionJpaRepository.existsByName(name);
    }

    @Override
    public kh.edu.istad.iam.domain.model.Permission save(kh.edu.istad.iam.domain.model.Permission permission) {
        Permission entity = permissionPersistenceMapper.toEntity(permission);
        Permission saved = permissionJpaRepository.save(entity);
        return permissionPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Permission> findById(Long id) {
        return permissionJpaRepository.findById(id).map(permissionPersistenceMapper::toDomain);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Permission> findByUuid(UUID uuid) {
        return permissionJpaRepository.findByUuid(uuid).map(permissionPersistenceMapper::toDomain);
    }

    @Override
    public Optional<kh.edu.istad.iam.domain.model.Permission> findByName(String name) {
        return permissionJpaRepository.findByName(name).map(permissionPersistenceMapper::toDomain);
    }

    @Override
    public List<kh.edu.istad.iam.domain.model.Permission> findAll(int page, int size) {
        return permissionJpaRepository.findAllByIsDeletedFalse(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(permissionPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public long count() {
        return permissionJpaRepository.count();
    }
}

