package kh.edu.istad.platform.business.domain.dto;

import java.util.List;
import java.util.UUID;

public record RegisterBusinessResult(
        UUID businessId,
        String legalName,
        String displayName,
        String email,
        String phoneNumber,
        boolean active,
        List<ProductResult> products,
        String message
) {
}
