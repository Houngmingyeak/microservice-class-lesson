package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.entity.entity.Customer;
import kh.edu.istad.platform.customer.domain.entity.valueobject.Email;
import kh.edu.istad.platform.customer.domain.entity.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistence.entity.CustomerEntity;
import kh.edu.istad.platform.customer.persistence.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepositoryAdapter(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setCustomerId(customer.getId().uuid());
        customerEntity.setUsername(customer.getUsername());
        customerEntity.setFamilyName(customer.getFamilyName());
        customerEntity.setGivenName(customer.getGivenName());
        customerEntity.setEmail(customer.getEmail().value());
        customerEntity.setPhoneNumber(customer.getPhoneNumber().value());

        customerJpaRepository.save(customerEntity);

        return customer;
    }

    @Override
    public Optional<Customer> findById(Customerid customerId) {
        return customerJpaRepository.findById(customerId.uuid())
                .map(entity -> Customer.Builder.builder()
                        .customerid(new Customerid(entity.getCustomerId()))
                        .username(entity.getUsername())
                        .familyName(entity.getFamilyName())
                        .givenName(entity.getGivenName())
                        .email(new Email(entity.getEmail()))
                        .phoneNumber(new PhoneNumber(entity.getPhoneNumber()))
                        .build());
    }
}