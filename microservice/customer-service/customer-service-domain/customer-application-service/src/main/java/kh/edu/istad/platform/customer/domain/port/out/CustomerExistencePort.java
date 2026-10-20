package kh.edu.istad.platform.customer.domain.port.out;

import reactor.core.publisher.Mono;

public interface CustomerExistencePort {
    Mono<Boolean> exists(String customerId);
}
