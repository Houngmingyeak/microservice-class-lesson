package kh.edu.istad.platform.customer.domain.entity.service;

import kh.edu.istad.platform.customer.domain.entity.entity.Customer;
import kh.edu.istad.platform.customer.domain.entity.event.CustomerDeactivatedEvent;
import kh.edu.istad.platform.customer.domain.entity.event.CustomerInitiatedEvent;
import kh.edu.istad.platform.customer.domain.entity.event.CustomerUpdatedEvent;

public interface CustomerDomainService {

    CustomerInitiatedEvent initiateCustomer(Customer customer);
    CustomerUpdatedEvent updateCustomer(String familyName, String givenName);
    CustomerDeactivatedEvent deactivateCustomer();

}
