package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.platform.customer.domain.entity.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;

public class CustomerRepositoryAdapter implements CustomerRepository {
    @Override
    public Customer save(Customer customer) {
        return null;
    }
}
