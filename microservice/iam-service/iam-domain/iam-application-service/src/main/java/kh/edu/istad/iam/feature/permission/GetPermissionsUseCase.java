package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.domain.Permission;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case: Get paginated list of active permissions.
 */
@Service
@RequiredArgsConstructor
public class GetPermissionsUseCase {

    private final PermissionJpaRepository permissionJpaRepository;

    @Transactional(readOnly = true)
    public PageResponse<PermissionResponse> execute(Pageable pageable) {
        Page<Permission> page = permissionJpaRepository.findAllByIsDeletedFalse(pageable);
        return new PageResponse<>(
                page.getContent().stream().map(CreatePermissionUseCase::toResponse).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
