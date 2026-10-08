package kh.edu.istad.platform.customer.domain.entity.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.customer.domain.entity.entity.Customer;

import java.time.ZonedDateTime;

public class CustomerDeactivatedEvent implements DomainEvent<Customer> {

    private final Customer customer;
    private final ZonedDateTime deactivatedAt;

    public CustomerDeactivatedEvent(Customer customer, ZonedDateTime deactivatedAt) {
        this.customer = customer;
        this.deactivatedAt = deactivatedAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ZonedDateTime getDeactivatedAt() {
        return deactivatedAt;
    }
}