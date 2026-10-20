package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCaseImpl implements UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;

    @Override
    public Mono<Customer> execute(UpdateCustomerCommand command) {
        return customerRepository.findById(new Customerid(command.customerId()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "Customer not found: " + command.customerId())))
                .flatMap(customer -> {
                    customer.updateCustomer(command.familyName(), command.givenName());
                    return customerRepository.update(customer);
                });
    }
}