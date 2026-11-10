package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.domain.User;
import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case: Get paginated list of active (non-deleted) users.
 */
@Service
@RequiredArgsConstructor
public class GetUsersUseCase {

    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> execute(Pageable pageable) {
        Page<User> page = userJpaRepository.findAllByIsDeletedFalse(pageable);
        return new PageResponse<>(
                page.getContent().stream().map(CreateUserUseCase::toResponse).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
