package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.entity.Customer;
import kh.edu.istad.platform.customer.domain.entity.service.CustomerDomainService;
import kh.edu.istad.platform.customer.domain.entity.valueobject.Email;
import kh.edu.istad.platform.customer.domain.entity.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitiateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public InitiateCustomerResult execute(InitiateCustomerCommand command) {
        log.info("initiate customer usecase: {}", command);

        Customer customer = Customer.Builder.builder()
                .username(command.username())
                .familyName(command.familyName())
                .givenName(command.givenName())
                .email(new Email(command.email()))
                .phoneNumber(new PhoneNumber(command.phoneNumber()))
                .build();

        customerDomainService.initiateCustomer(customer);

        Customer savedCustomer = customerRepository.save(customer);

        return new InitiateCustomerResult(
                savedCustomer.getId().uuid(),
                savedCustomer.getUsername(),
                savedCustomer.getFamilyName(),
                savedCustomer.getGivenName(),
                savedCustomer.getEmail().value(),
                savedCustomer.getPhoneNumber().value()
        );
    }

}