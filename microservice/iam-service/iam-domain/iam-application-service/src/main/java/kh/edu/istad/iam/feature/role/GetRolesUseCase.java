package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.domain.Role;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case: Get paginated list of active roles.
 */
@Service
@RequiredArgsConstructor
public class GetRolesUseCase {

    private final RoleJpaRepository roleJpaRepository;

    @Transactional(readOnly = true)
    public PageResponse<RoleResponse> execute(Pageable pageable) {
        Page<Role> page = roleJpaRepository.findAllByIsDeletedFalse(pageable);
        return new PageResponse<>(
                page.getContent().stream().map(CreateRoleUseCase::toResponse).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
