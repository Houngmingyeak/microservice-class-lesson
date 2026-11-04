package kh.edu.istad.platform.order.client.httpclient;

import kh.edu.istad.platform.order.domain.dto.QueryCustomerResult;
import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange("/api/customers")
@ClientRegistrationId("customer-service-client")
public interface CustomerHttpClient {

    @GetExchange("/customers/{id}")
    CustomerServiceClient.CustomerResponse getCustomer(@PathVariable Long id);

    @GetExchange
    Flux<QueryCustomerResult> queryCustomers();

    @GetExchange("/{id}")
    Mono<QueryCustomerResult> getCustomerById(@PathVariable String id);

    @PostExchange
    Mono<Void> createCustomer(@RequestBody QueryCustomerResult customer);
}
