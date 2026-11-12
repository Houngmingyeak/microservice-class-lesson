//package kh.edu.istad.iam.web;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.MessageSource;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Locale;
//
//@Controller
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final MessageSource messageSource;
//
//    @GetMapping("/login")
//    public String login(@RequestParam(required = false) String error, Locale locale, Model model) {
//        model.addAttribute("title", messageSource.getMessage("login.title", null, "Sign in", locale));
//        model.addAttribute("error", error == null ? null
//                : messageSource.getMessage("login.error", null, "Invalid username or password", locale));
//        model.addAttribute("locale", locale.getLanguage());
//        return "login";
//    }
//}

package kh.edu.istad.iam.web;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // នៅពេលអ្នកចូល http://localhost:9000/login method នេះនឹងដំណើរការ
    @GetMapping("/login")
    public String login(
            // Spring Security បញ្ជូនមក /login?error ពេលបញ្ចូលពាក្យសម្ងាត់ខុស
            @RequestParam(value = "error", required = false) String error,
            // Spring Security បញ្ជូនមក /login?logout ពេល logout ជោគជ័យ
            @RequestParam(value = "logout", required = false) String logout,
            // CSRF token ដែល form POST ត្រូវការ បើគ្មានវាទេ login នឹងបរាជ័យ (403)
            CsrfToken csrfToken,
            Model model
    ) {

        // បើមាន ?error នៅក្នុង URL នោះ hasError = true
        model.addAttribute("hasError", error != null);

        // បើមាន ?logout នៅក្នុង URL នោះ loggedOut = true
        model.addAttribute("loggedOut", logout != null);

        // ផ្ញើឈ្មោះ និងតម្លៃ CSRF ទៅ login.jte
        model.addAttribute("csrfParameterName", csrfToken.getParameterName());
        model.addAttribute("csrfToken", csrfToken.getToken());

        // "login" គឺជាឈ្មោះ template => ស្វែងរក login.jte
        return "login";
    }
}