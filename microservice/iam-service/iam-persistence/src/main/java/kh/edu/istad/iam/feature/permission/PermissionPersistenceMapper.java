package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionPersistenceMapper {

    public kh.edu.istad.iam.domain.model.Permission toDomain(Permission entity) {
        if (entity == null) return null;
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

    public Permission toEntity(kh.edu.istad.iam.domain.model.Permission model) {
        if (model == null) return null;
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

