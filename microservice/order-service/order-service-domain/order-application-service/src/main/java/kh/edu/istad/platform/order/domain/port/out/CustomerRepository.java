package kh.edu.istad.platform.order.domain.port.out;

import kh.edu.istad.platform.order.domain.entity.Customer;
import kh.edu.istad.platform.order.domain.valueobject.CustomerId;

import java.util.Optional;

public interface CustomerRepository {

    Optional<Customer> findCustomer(CustomerId customerId);

}
