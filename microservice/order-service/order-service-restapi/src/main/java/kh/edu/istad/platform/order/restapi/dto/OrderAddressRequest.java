package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrderAddressRequest(
        @NotBlank(message = "street is required")
        String street,
        @NotBlank(message = "postalCode is required")
        @Size(max = 10, message = "postalCode must not be longer than 10 characters")
        String postalCode,
        @NotBlank(message = "city is required")
        String city
) {
}
