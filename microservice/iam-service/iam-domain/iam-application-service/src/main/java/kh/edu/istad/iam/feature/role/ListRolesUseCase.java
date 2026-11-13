package kh.edu.istad.iam.feature.role;

import kh.edu.istad.iam.shared.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListRolesUseCase {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public PageResult<RoleSummary> execute(RoleFilterQuery query) {
        long total = roleRepository.count();
        int totalPages = (int) Math.ceil((double) total / query.size());

        List<RoleSummary> content = roleRepository.findAll(query.page(), query.size()).stream()
                .map(r -> new RoleSummary(r.getUuid(), r.getName(), r.getIsActive()))
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
    public PageResult<RoleSummary> execute(int page, int size) {
        return execute(new RoleFilterQuery(page, size));
    }
}
