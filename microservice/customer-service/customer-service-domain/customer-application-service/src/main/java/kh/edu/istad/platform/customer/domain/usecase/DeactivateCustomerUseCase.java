package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
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

//    public void execute(UUID customerId) {
//        log.info("deactivate customer usecase: {}", customerId);
//
//        Customer customer = customerRepository.findById(new CustomerId(customerId))
//                .orElseThrow(() -> new CustomerDomainException(
//                        "Customer not found: " + customerId));
//
//        customerDomainService.deactivateCustomer(customer);
//
//        customerRepository.save(customer);
//    }
}
