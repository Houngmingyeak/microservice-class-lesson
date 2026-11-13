package kh.edu.istad.iam.feature;

import kh.edu.istad.iam.domain.Permission;
import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.feature.permission.PermissionPersistenceMapper;
import kh.edu.istad.iam.feature.role.RolePersistenceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceMappersTest {

    private final RolePersistenceMapper roleMapper = new RolePersistenceMapper();
    private final PermissionPersistenceMapper permissionMapper = new PermissionPersistenceMapper();

    @Test
    @DisplayName("RolePersistenceMapper converts between domain model and JPA entity")
    void testRoleMapper() {
        kh.edu.istad.iam.domain.model.Role domainModel = new kh.edu.istad.iam.domain.model.Role();
        domainModel.setId(1L);
        domainModel.setUuid(UUID.randomUUID());
        domainModel.setName("ADMIN");
        domainModel.setDescription("Administrator role");
        domainModel.setIsSystem(true);
        domainModel.setIsActive(true);

        Role entity = roleMapper.toEntity(domainModel);
        assertNotNull(entity);
        assertEquals(domainModel.getName(), entity.getName());
        assertEquals(domainModel.getUuid(), entity.getUuid());

        kh.edu.istad.iam.domain.model.Role backToDomain = roleMapper.toDomain(entity);
        assertNotNull(backToDomain);
        assertEquals(entity.getName(), backToDomain.getName());
        assertEquals(entity.getUuid(), backToDomain.getUuid());
    }

    @Test
    @DisplayName("PermissionPersistenceMapper converts between domain model and JPA entity")
    void testPermissionMapper() {
        kh.edu.istad.iam.domain.model.Permission domainModel = new kh.edu.istad.iam.domain.model.Permission();
        domainModel.setId(2L);
        domainModel.setUuid(UUID.randomUUID());
        domainModel.setName("user:read");
        domainModel.setResource("user");
        domainModel.setAction("read");

        Permission entity = permissionMapper.toEntity(domainModel);
        assertNotNull(entity);
        assertEquals("user:read", entity.getName());

        kh.edu.istad.iam.domain.model.Permission backToDomain = permissionMapper.toDomain(entity);
        assertNotNull(backToDomain);
        assertEquals("user:read", backToDomain.getName());
    }
}

