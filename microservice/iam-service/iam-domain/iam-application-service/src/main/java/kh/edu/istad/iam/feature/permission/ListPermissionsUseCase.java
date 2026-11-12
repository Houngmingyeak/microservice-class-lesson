package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPermissionsUseCase {

    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public PageResponse<PermissionResult> execute(int page, int size) {
        long total = permissionRepository.count();
        int totalPages = (int) Math.ceil((double) total / size);

        List<PermissionResult> content = permissionRepository.findAll(page, size).stream()
                .map(p -> new PermissionResult(
                        p.getUuid(),
                        p.getName(),
                        p.getResource(),
                        p.getAction(),
                        p.getDescription(),
                        p.getIsSystem(),
                        p.getIsActive(),
                        p.getCreatedAt(),
                        p.getUpdatedAt()
                ))
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

