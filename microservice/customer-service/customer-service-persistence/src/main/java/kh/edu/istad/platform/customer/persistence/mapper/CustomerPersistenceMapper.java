////package kh.edu.istad.platform.customer.persistence.mapper;
////
////import org.mapstruct.Mapper;
////
////@Mapper(componentModel = "spring")
////public interface CustomerPersistenceMapper {
////
////}
//package kh.edu.istad.platform.customer.persistence.mapper;
//
//import kh.edu.istad.common.domain.valueobject.Customerid;
//import kh.edu.istad.platform.customer.domain.entity.entity.Customer;
////import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
////import kh.edu.istad.platform.customer.domain.valueobject.Email;
////import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
//import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomerPersistenceMapper {
//
//    public CustomerDocument toDocument(Customer c) {
//        CustomerDocument d = new CustomerDocument();
//        d.setCustomerId(c.getId().uuid());
//        d.setUsername(c.getUsername());
//        d.setFamilyName(c.getFamilyName());
//        d.setGivenName(c.getGivenName());
//        d.setEmail(c.getEmail().value());
//        d.setPhoneNumber(c.getPhoneNumber().value());
//        d.setStatus(c.getCustomerStatus().name());
//        return d;
//    }
//
//    // ADAPT: same builder syntax as in CreateCustomerUseCaseImpl
//    public Customer toDomain(CustomerDocument d) {
//        return Customer.builder()
//                .id(new Customerid(d.getCustomerId()))
//                .username(d.getUsername())
//                .familyName(d.getFamilyName())
//                .givenName(d.getGivenName())
//                .email(new Email(d.getEmail()))
//                .phoneNumber(new PhoneNumber(d.getPhoneNumber()))
//                .customerStatus(CustomerStatus.valueOf(d.getStatus()))
//                .build();
//    }
//}

package kh.edu.istad.platform.customer.persistence.mapper;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerPersistenceMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "phoneNumber.value", target = "phoneNumber")
    CustomerDocument toDocument(Customer customer);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "email", target = "email.value")
    @Mapping(source = "phoneNumber", target = "phoneNumber.value")
    Customer toDomain(CustomerDocument customerDocument);
}