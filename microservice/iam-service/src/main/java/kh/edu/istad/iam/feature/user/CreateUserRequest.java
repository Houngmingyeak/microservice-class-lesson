package kh.edu.istad.iam.feature.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateUserRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        @NotBlank(message = "Given name is required")
        String givenName,

        @NotBlank(message = "Family name is required")
        String familyName,

        String gender,

        LocalDate birthdate,

        String phoneNumber
) {
}
