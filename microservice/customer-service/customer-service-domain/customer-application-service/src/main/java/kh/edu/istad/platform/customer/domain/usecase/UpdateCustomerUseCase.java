package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
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
public class UpdateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public UpdateCustomerResult execute(UpdateCustomerCommand command) {
        log.info("update customer usecase: {}", command);

        Customer customer = customerRepository.findById(new Customerid(command.customerId()))
                .orElseThrow(() -> new CustomerDomainException("Customer not found with id: " + command.customerId()));

        customer.updateCustomer(command.familyName(), command.givenName());
        customerDomainService.updateCustomer(customer);

        Customer savedCustomer = customerRepository.save(customer);

        return new UpdateCustomerResult(
                savedCustomer.getId().uuid(),
                savedCustomer.getUsername(),
                savedCustomer.getFamilyName(),
                savedCustomer.getGivenName(),
                savedCustomer.getEmail().value(),
                savedCustomer.getPhoneNumber().value(),
                savedCustomer.getCustomerStatus().name()
        );
    }
}