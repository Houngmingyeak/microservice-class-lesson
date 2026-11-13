package kh.edu.istad.iam.feature.user;

import kh.edu.istad.iam.config.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @InjectMocks
    private AuthRestController authRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authRestController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/auth/register returns 201 when payload is valid")
    void testRegisterSuccess() throws Exception {
        UserResult result = new UserResult(
                UUID.randomUUID(),
                "sokdara",
                "dara.sok@istad.co",
                false,
                "Dara",
                "Sok",
                null,
                null,
                null,
                false,
                null,
                true
        );

        when(registerUserUseCase.execute(any())).thenReturn(result);

        String jsonPayload = """
                {
                    "username": "sokdara",
                    "email": "dara.sok@istad.co",
                    "password": "Str0ng!Pass2026",
                    "confirmPassword": "Str0ng!Pass2026",
                    "givenName": "Dara",
                    "familyName": "Sok",
                    "locale": "km"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("sokdara"))
                .andExpect(jsonPath("$.email").value("dara.sok@istad.co"));
    }

    @Test
    @DisplayName("POST /api/auth/register returns RFC 9457 ProblemDetail on validation error")
    void testRegisterValidationErrorRfc9457() throws Exception {
        // Missing required fields & invalid email
        String invalidPayload = """
                {
                    "username": "",
                    "email": "invalid-email-format",
                    "password": "",
                    "confirmPassword": "",
                    "givenName": "",
                    "familyName": "",
                    "locale": "km"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://iam.istad.co/problems/validation-error"))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }
}
