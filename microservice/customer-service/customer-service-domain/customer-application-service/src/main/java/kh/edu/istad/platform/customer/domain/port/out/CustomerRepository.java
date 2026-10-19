package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.common.domain.valueobject.Customerid;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {
    Mono <Customer> save(Customer customer);

    Mono<Customer> findById(Customerid customerid);

    Flux<Customer> findAll();
}
