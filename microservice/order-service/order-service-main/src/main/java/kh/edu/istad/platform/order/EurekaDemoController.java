package kh.edu.istad.platform.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo")
public class EurekaDemoController {

    private final RestClient.Builder builder;

    public EurekaDemoController(RestClient.Builder builder) {
        this.builder = builder;
    }

    @GetMapping("/eureka")
    public Map<String, Object> call() {
        return Map.of(
                "customer", get("http://customer-service"),
                "business", get("http://business-service"),
                "payment",  get("http://payment-service"));
    }

    private Map<?, ?> get(String baseUrl) {
        return builder.baseUrl(baseUrl).build().get()
                .uri("/api/v1/instance-info").retrieve().body(Map.class);
    }
}