package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.shared.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageResponse<UserSummary> execute(int page, int size, String sortBy, String direction) {
        var users = userRepository.findAll(page, size, sortBy, direction);
        long total = userRepository.count();
        int totalPages = (int) Math.ceil((double) total / size);

        List<UserSummary> summaries = users.stream().map(u -> new UserSummary(
                u.getUuid(),
                u.getUsername(),
                u.getEmail(),
                (u.getGivenName() != null ? u.getGivenName() : "") + " " + (u.getFamilyName() != null ? u.getFamilyName() : ""),
                u.getLocale(),
                u.getEnabled(),
                u.getCreatedAt()
        )).toList();

        return new PageResponse<>(summaries, page, size, total, totalPages, page >= totalPages - 1);
    }
}

