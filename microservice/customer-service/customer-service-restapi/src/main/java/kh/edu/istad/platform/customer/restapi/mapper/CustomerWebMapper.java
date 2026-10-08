package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.dto.*;
import kh.edu.istad.platform.customer.restapi.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper {

    InitiateCustomerCommand toCommand(CustomerInitiateRequest request);

    CustomerInitiateResponse toResponse(InitiateCustomerResult result);

    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "familyName", source = "request.familyName")
    @Mapping(target = "givenName", source = "request.givenName")
    UpdateCustomerCommand toCommand(UUID customerId, CustomerUpdateRequest request);

    CustomerUpdateResponse toResponse(UpdateCustomerResult result);

    default DeactivateCustomerCommand toDeactivateCommand(UUID customerId) {
        return new DeactivateCustomerCommand(customerId);
    }

    CustomerDeactivateResponse toResponse(DeactivateCustomerResult result);
}