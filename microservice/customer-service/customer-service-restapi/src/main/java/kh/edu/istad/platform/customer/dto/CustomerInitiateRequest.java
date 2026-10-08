package kh.edu.istad.platform.customer.dto;

import jakarta.validation.constraints.NotNull;

public record CustomerInitiateRequest(
        @NotNull
        String username,
        @NotNull
        String familyName,
        String givenName,
        String email,
        String phoneNumber
) {
}
