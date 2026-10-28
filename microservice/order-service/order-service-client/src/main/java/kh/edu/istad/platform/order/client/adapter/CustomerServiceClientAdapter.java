package kh.edu.istad.platform.order.client.adapter;

import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class CustomerServiceClientAdapter implements CustomerServiceClient {

    private final WebClient.Builder webClientBuilder;

    public CustomerServiceClientAdapter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // Changed to plain Strings if the API returns strings
    private record CustomerPayload(
            String id,
            String username,
            String familyName,
            String givenName,
            String email,
            String phoneNumber,
            String status
    ) {}

    @Override
    public Flux<CustomerResponse> getCustomers() {
        return webClientBuilder
                .baseUrl("http://customer-service")
                .build()
                .get()
                .uri("/api/customers")
                .retrieve()
                .bodyToFlux(CustomerPayload.class)
                .map(customer -> new CustomerResponse(
                        customer.id(),
                        customer.username(),
                        customer.familyName(),
                        customer.givenName(),
                        customer.email(),
                        customer.phoneNumber(),
                        customer.status(),
                        null
                ));
    }
}