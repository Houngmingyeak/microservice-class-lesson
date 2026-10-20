package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import reactor.core.publisher.Mono;

public interface UpdateCustomerUseCase {
    Mono<Customer> execute(UpdateCustomerCommand command);
}