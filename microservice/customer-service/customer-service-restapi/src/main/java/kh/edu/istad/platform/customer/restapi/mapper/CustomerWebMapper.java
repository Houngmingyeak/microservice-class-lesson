package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.customer.restapi.dto.CustomerDeactivateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerWebMapper {

    default Email toEmail(String email) {
        return new Email(email);
    }

    default PhoneNumber toPhoneNumber(String phoneNumber) {
        return new PhoneNumber(phoneNumber);
    }

    default String fromEmail(Email email) {
        return email == null ? null : email.value();
    }

    default String fromPhoneNumber(PhoneNumber phoneNumber) {
        return phoneNumber == null ? null : phoneNumber.value();
    }

    InitiateCustomerCommand toCommand(CustomerInitiateRequest request);

    @Mapping(target = "customerId", source = "id")
    UpdateCustomerCommand toUpdateCommand(UUID id, CustomerUpdateRequest request);

    CustomerInitiateResponse toResponse(InitiateCustomerResult result);

    @Mapping(target = "customerId", expression = "java(customer.getId().value())")
    CustomerInitiateResponse customerToResponse(Customer customer);

    @Mapping(target = "customerId", expression = "java(customer.getId().value())")
    CustomerUpdateResponse toUpdateResponse(Customer customer);

    @Mapping(target = "customerId", expression = "java(customer.getId().value())")
    CustomerDeactivateResponse toDeactivateResponse(Customer customer);
}