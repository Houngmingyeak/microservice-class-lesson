package kh.edu.istad.platform.business.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegisterBusinessRequest(
        @NotBlank(message = "legalName is required")
        @Size(max = 100, message = "legalName must not be longer than 100 characters")
        String legalName,
        @NotBlank(message = "displayName is required")
        @Size(max = 100, message = "displayName must not be longer than 100 characters")
        String displayName,
        @NotBlank(message = "email is required")
        @Email(message = "email is not valid")
        String email,
        @NotBlank(message = "phoneNumber is required")
        String phoneNumber,
        @Valid
        List<ProductRequest> products
) {
}
