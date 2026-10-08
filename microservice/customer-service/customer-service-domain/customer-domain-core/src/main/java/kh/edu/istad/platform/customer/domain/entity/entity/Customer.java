package kh.edu.istad.platform.customer.domain.entity.entity;

import kh.edu.istad.common.domain.valueobject.entity.AggregateRoot;

import kh.edu.istad.common.domain.valueobject.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.entity.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.entity.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.entity.valueobject.Email;
import kh.edu.istad.platform.customer.domain.entity.valueobject.PhoneNumber;

import java.util.Objects;
import java.util.UUID;

public class Customer extends AggregateRoot<Customerid> {
    private final Customerid customerid;
    private final String username;
    private String familyName;
    private String givenName;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private CustomerStatus customerStatus;

    //domain critical logic

    public void updateCustomer( String familyName, String givenName  ){
        if (familyName == null || givenName == null){
            throw new CustomerDomainException(" familyName and givenName must be null");
        }
        this.familyName = familyName;
        this.givenName = givenName;
    }





    public void iniciateCustomer(){
        validateCustomer();
        super.setId(new Customerid(UUID.randomUUID()));
        customerStatus = CustomerStatus.ACTIVE;
    }

    private void validateCustomer() {
        if (super.getId() !=null){
            throw new CustomerDomainException("Customer ID must be null");

        }
        if (customerStatus != null){
            throw new CustomerDomainException("Customer Status must be null");
        }

    }

    public String getUsername() {
        return username;
    }

    public Customerid getCustomerid() {
        return customerid;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }

    private Customer(Builder builder) {
        customerid = builder.customerid;
        username = builder.username;
        familyName = builder.familyName;
        givenName = builder.givenName;
        email = builder.email;
        phoneNumber = builder.phoneNumber;
        customerStatus = builder.customerStatus;
    }


    public static final class Builder {
        private Customerid customerid;
        private String username;
        private String familyName;
        private String givenName;
        private Email email;
        private PhoneNumber phoneNumber;
        private CustomerStatus customerStatus;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder customerid(Customerid val) {
            customerid = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder familyName(String val) {
            familyName = val;
            return this;
        }

        public Builder givenName(String val) {
            givenName = val;
            return this;
        }

        public Builder email(Email val) {
            email = val;
            return this;
        }

        public Builder phoneNumber(PhoneNumber val) {
            phoneNumber = val;
            return this;
        }

        public Builder customerStatus(CustomerStatus val) {
            customerStatus = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerid, customer.customerid) && Objects.equals(username, customer.username) && Objects.equals(familyName, customer.familyName) && Objects.equals(givenName, customer.givenName) && Objects.equals(email, customer.email) && Objects.equals(phoneNumber, customer.phoneNumber) && customerStatus == customer.customerStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerid, username, familyName, givenName, email, phoneNumber, customerStatus);
    }
}
