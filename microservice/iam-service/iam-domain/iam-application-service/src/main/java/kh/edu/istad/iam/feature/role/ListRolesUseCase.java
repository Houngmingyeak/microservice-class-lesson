package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListRolesUseCase {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public PageResponse<RoleSummary> execute(int page, int size) {
        long total = roleRepository.count();
        int totalPages = (int) Math.ceil((double) total / size);

        List<RoleSummary> content = roleRepository.findAll(page, size).stream()
                .map(r -> new RoleSummary(r.getUuid(), r.getName(), r.getIsActive()))
                .toList();

        return new PageResponse<>(
                content,
                page,
                size,
                total,
                totalPages,
                page == 0,
                page >= totalPages - 1
        );
    }
}

