package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.platform.order.domain.entity.Customer;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.order.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.persistence.entity.CustomerEntity;
import kh.edu.istad.platform.order.persistence.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepositoryAdapter(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findCustomer(CustomerId customerId) {
        Optional<CustomerEntity> optionalEntity = customerJpaRepository.findById(customerId.id());
        if (optionalEntity.isEmpty()) {
            return Optional.empty();
        }
        CustomerEntity customerEntity = optionalEntity.get();

        Customer customer = new Customer(new CustomerId(customerEntity.getCustomerId()));

        return Optional.of(customer);
    }
}
