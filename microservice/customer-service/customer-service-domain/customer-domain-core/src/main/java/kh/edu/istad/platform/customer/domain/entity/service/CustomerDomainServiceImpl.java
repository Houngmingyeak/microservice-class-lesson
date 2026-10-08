package kh.edu.istad.platform.customer.domain.entity.service;

import kh.edu.istad.platform.customer.domain.entity.entity.Customer;
import kh.edu.istad.platform.customer.domain.entity.event.CustomerDeactivatedEvent;
import kh.edu.istad.platform.customer.domain.entity.event.CustomerInitiatedEvent;
import kh.edu.istad.platform.customer.domain.entity.event.CustomerUpdatedEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CustomerDomainServiceImpl implements CustomerDomainService{
    @Override
    public CustomerInitiatedEvent initiateCustomer(Customer customer) {
        customer.initiateCustomer();
        return new CustomerInitiatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerUpdatedEvent updateCustomer(Customer customer) {
        customer.updateCustomer(customer.getFamilyName(), customer.getGivenName());
        return new CustomerUpdatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public CustomerDeactivatedEvent deactivateCustomer(Customer customer) {
        customer.deactivateCustomer();
        return new CustomerDeactivatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

}
