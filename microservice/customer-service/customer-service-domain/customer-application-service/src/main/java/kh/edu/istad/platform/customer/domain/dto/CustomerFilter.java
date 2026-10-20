package kh.edu.istad.platform.customer.domain.dto;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;

import java.time.Instant;

public record CustomerFilter(
        String keyword,
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        CustomerStatus status,
        Instant createdFrom,
        Instant createdTo,
        int page,
        int size,
        String sortBy,
        String sortDir
) {
    public CustomerFilter {
        page = Math.max(page, 0);
        size = (size <= 0) ? 10 : Math.min(size, 100);
        sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;
        sortDir = (sortDir == null || sortDir.isBlank()) ? "desc" : sortDir;
    }
}