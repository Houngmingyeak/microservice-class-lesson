package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.dto.PageResult;

@Service
@RequiredArgsConstructor
public class QueryRepositoryUseCase {

    private final CustomerRepository customerRepository;

    public Flux<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Flux<Customer> query(String username) {
        return customerRepository.query(username);
    }

    public Mono<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public Mono<Void> deleteById(Customerid id) {
        return customerRepository.deleteById(id);
    }

    public Mono<PageResult<Customer>> search(CustomerFilter filter) {
        return customerRepository.search(filter);
    }
}