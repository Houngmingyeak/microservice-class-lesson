package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.platform.customer.domain.port.out.CustomerExistencePort;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CustomerExistenceAdapter implements CustomerExistencePort {

    private final CustomerMongoRepository customers;

    public CustomerExistenceAdapter(CustomerMongoRepository customers) {
        this.customers = customers;
    }

    @Override
    public Mono<Boolean> exists(String customerId) {
        return customers.existsById(UUID.fromString(customerId));
    }
}