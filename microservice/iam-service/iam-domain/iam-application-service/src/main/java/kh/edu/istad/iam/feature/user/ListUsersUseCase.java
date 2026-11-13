package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.shared.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResult<UserSummary> execute(UserFilterQuery query) {
        var users = userRepository.findAll(query.page(), query.size(), query.sortBy(), query.direction());
        long total = userRepository.count();
        int totalPages = (int) Math.ceil((double) total / query.size());

        List<UserSummary> summaries = users.stream().map(u -> new UserSummary(
                u.getUuid(),
                u.getUsername(),
                u.getEmail(),
                (u.getGivenName() != null ? u.getGivenName() : "") + " " + (u.getFamilyName() != null ? u.getFamilyName() : ""),
                u.getLocale(),
                u.getEnabled(),
                u.getCreatedAt()
        )).toList();

        return new PageResult<>(summaries, query.page(), query.size(), total, totalPages, query.page() == 0, query.page() >= totalPages - 1);
    }

    @Transactional(readOnly = true)
    public PageResult<UserSummary> execute(int page, int size, String sortBy, String direction) {
        return execute(new UserFilterQuery(page, size, sortBy, direction));
    }
}
