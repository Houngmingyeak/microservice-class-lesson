package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.dto.DeactivateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.DeactivateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.entity.Customer;
import kh.edu.istad.platform.customer.domain.entity.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.entity.service.CustomerDomainService;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeactivateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public DeactivateCustomerResult execute(DeactivateCustomerCommand command) {
        log.info("deactivate customer usecase: {}", command);

        Customer customer = customerRepository.findById(new Customerid(command.customerId()))
                .orElseThrow(() -> new CustomerDomainException("Customer not found with id: " + command.customerId()));

        customerDomainService.deactivateCustomer(customer);

        Customer savedCustomer = customerRepository.save(customer);

        return new DeactivateCustomerResult(
                savedCustomer.getId().uuid(),
                savedCustomer.getCustomerStatus().name()
        );
    }
}