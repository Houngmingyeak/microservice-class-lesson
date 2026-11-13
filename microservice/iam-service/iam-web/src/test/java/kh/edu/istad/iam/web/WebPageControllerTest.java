package kh.edu.istad.iam.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class WebPageControllerTest {

    @InjectMocks
    private WebPageController webPageController;

    @Test
    @DisplayName("login() returns 'login' view and sets locale attribute")
    void testLoginView() {
        Model model = new ConcurrentModel();
        String view = webPageController.login(null, null, null, Locale.ENGLISH, model);

        assertEquals("login", view);
        assertEquals("en", model.getAttribute("locale"));
    }

    @Test
    @DisplayName("registerPage() returns 'register' view and sets locale attribute")
    void testRegisterView() {
        Model model = new ConcurrentModel();
        String view = webPageController.registerPage(null, Locale.forLanguageTag("km"), model);

        assertEquals("register", view);
        assertEquals("km", model.getAttribute("locale"));
    }
}
