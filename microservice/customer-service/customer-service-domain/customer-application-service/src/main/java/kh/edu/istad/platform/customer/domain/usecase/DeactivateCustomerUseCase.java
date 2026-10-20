package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import reactor.core.publisher.Mono;

public interface DeactivateCustomerUseCase {
    Mono<Customer> execute(Customerid id);
}