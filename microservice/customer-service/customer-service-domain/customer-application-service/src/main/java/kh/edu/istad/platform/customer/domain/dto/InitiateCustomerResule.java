package kh.edu.istad.platform.customer.domain.dto;

import java.util.UUID;

public record InitiateCustomerResule(
        UUID customerId,
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber
) {
}
