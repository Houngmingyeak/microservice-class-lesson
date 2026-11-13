package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class RolePersistenceMapper {

    public kh.edu.istad.iam.domain.model.Role toDomain(Role entity) {
        if (entity == null) return null;
        kh.edu.istad.iam.domain.model.Role model = new kh.edu.istad.iam.domain.model.Role();
        model.setId(entity.getId());
        model.setUuid(entity.getUuid());
        model.setName(entity.getName());
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

    public Role toEntity(kh.edu.istad.iam.domain.model.Role model) {
        if (model == null) return null;
        Role entity = new Role();
        entity.setId(model.getId());
        entity.setUuid(model.getUuid());
        entity.setName(model.getName());
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

