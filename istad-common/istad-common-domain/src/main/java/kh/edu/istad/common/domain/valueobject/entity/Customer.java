package kh.edu.istad.common.domain.valueobject.entity;

import kh.edu.istad.common.domain.valueobject.valueobject.CustomerStatus;
import kh.edu.istad.common.domain.valueobject.valueobject.Customerid;
import kh.edu.istad.common.domain.valueobject.valueobject.Email;
import kh.edu.istad.common.domain.valueobject.valueobject.PhoneNumber;

public class Customer {
    private final Customerid customerid;
    private final String username;
    private String familyName;
    private String givenName;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private final CustomerStatus customerStatus;

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



}
