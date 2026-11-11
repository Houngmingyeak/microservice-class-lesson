package kh.edu.istad.iam.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MessageSource messageSource;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Locale locale, Model model) {
        model.addAttribute("title", messageSource.getMessage("login.title", null, "Sign in", locale));
        model.addAttribute("error", error == null ? null
                : messageSource.getMessage("login.error", null, "Invalid username or password", locale));
        model.addAttribute("locale", locale.getLanguage());
        return "login";
    }
}