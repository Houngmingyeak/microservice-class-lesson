package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.CreateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.CreateCustomerResult;
import reactor.core.publisher.Mono;

public interface CreateCustomerUseCase {

    Mono<CreateCustomerResult> execute(CreateCustomerCommand command);
}