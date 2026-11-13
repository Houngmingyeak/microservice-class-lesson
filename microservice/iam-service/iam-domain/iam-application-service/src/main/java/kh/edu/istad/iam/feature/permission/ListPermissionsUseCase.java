package kh.edu.istad.iam.feature.permission;

import kh.edu.istad.iam.shared.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPermissionsUseCase {

    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public PageResult<PermissionResult> execute(PermissionFilterQuery query) {
        long total = permissionRepository.count();
        int totalPages = (int) Math.ceil((double) total / query.size());

        List<PermissionResult> content = permissionRepository.findAll(query.page(), query.size()).stream()
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

        return new PageResult<>(
                content,
                query.page(),
                query.size(),
                total,
                totalPages,
                query.page() == 0,
                query.page() >= totalPages - 1
        );
    }

    @Transactional(readOnly = true)
    public PageResult<PermissionResult> execute(int page, int size) {
        return execute(new PermissionFilterQuery(page, size));
    }
}
