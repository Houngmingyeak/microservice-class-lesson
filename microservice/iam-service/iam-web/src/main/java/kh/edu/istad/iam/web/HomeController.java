package kh.edu.istad.iam.web;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home(Authentication authentication) {
        return Map.of(
                "message", "Login successful",
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities().toString()
        );
    }
}