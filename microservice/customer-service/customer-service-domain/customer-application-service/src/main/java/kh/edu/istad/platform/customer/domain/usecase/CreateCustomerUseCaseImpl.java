//package kh.edu.istad.platform.customer.domain.usecase;
//
//import kh.edu.istad.platform.customer.domain.dto.CreateCustomerCommand;
//import kh.edu.istad.platform.customer.domain.dto.CreateCustomerResult;
//import kh.edu.istad.platform.customer.domain.entity.entity.Customer;
//import kh.edu.istad.platform.customer.domain.entity.valueobject.Email;
//import kh.edu.istad.platform.customer.domain.entity.valueobject.PhoneNumber;
//import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
//import reactor.core.publisher.Mono;
//
//public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {
//
//    private final CustomerRepository customerRepository;
//
//    public CreateCustomerUseCaseImpl(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }
//
//    @Override
//    public Mono<CreateCustomerResult> execute(CreateCustomerCommand command) {
//        return Mono.fromCallable(() -> toDomain(command))
//                .flatMap(customerRepository::save)
//                .map(saved -> new CreateCustomerResult(
//                        saved.getId().uuid(),
//                        saved.getUsername(),
//                        saved.getEmail().value(),
//                        saved.getCustomerStatus().name()));
//    }
//
//    private Customer toDomain(CreateCustomerCommand c) {
//        Customer customer = Customer.Builder.builder()
//                .username(c.username())
//                .familyName(c.familyName())
//                .givenName(c.givenName())
//                .email(new Email(c.email()))
//                .phoneNumber(new PhoneNumber(c.phoneNumber()))
//                .build();
//
//        customer.initiateCustomer(); // sets a new UUID and status ACTIVE
//        return customer;
//    }
//}