package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeactivateCustomerUseCaseImpl implements DeactivateCustomerUseCase {

    private final CustomerRepository customerRepository;

    @Override
    public Mono<Customer> execute(Customerid id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "Customer not found: " + id)))
                .flatMap(customer -> {
                    customer.deactivate();
                    return customerRepository.update(customer);
                });
    }
}