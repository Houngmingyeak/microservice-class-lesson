package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.dto.PageResult;

public interface CustomerRepository {
    Mono<Customer> save(Customer customer);
    Mono<Customer> findById(Customerid customerId);
    Flux<Customer> findAll();
    Mono<Void> deleteById(Customerid customerId);
    Mono<Customer> update(Customer customer);
    Mono<Customer> findByUsername(String username);
    Flux<Customer> query(String username);
    Mono<PageResult<Customer>> search(CustomerFilter filter);
}