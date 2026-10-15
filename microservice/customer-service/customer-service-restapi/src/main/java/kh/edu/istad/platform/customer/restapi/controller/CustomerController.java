package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.customer.domain.dto.DeactivateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.DeactivateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerResult;
import kh.edu.istad.platform.customer.domain.usecase.DeactivateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.InitiateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.UpdateCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerDeactivateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateResponse;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final InitiateCustomerUseCase initiateCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;
    private final CustomerWebMapper customerWebMapper;

    public CustomerController(InitiateCustomerUseCase initiateCustomerUseCase,
                              UpdateCustomerUseCase updateCustomerUseCase,
                              DeactivateCustomerUseCase deactivateCustomerUseCase,
                              CustomerWebMapper customerWebMapper) {
        this.initiateCustomerUseCase = initiateCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
        this.deactivateCustomerUseCase = deactivateCustomerUseCase;
        this.customerWebMapper = customerWebMapper;
    }

    @PostMapping
    public ResponseEntity<CustomerInitiateResponse> initiate(@Valid @RequestBody CustomerInitiateRequest request) {
        InitiateCustomerCommand command = customerWebMapper.toCommand(request);
        InitiateCustomerResult result = initiateCustomerUseCase.execute(command);
        CustomerInitiateResponse response = customerWebMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerUpdateResponse> update(@PathVariable("customerId") UUID customerId,
                                                         @Valid @RequestBody CustomerUpdateRequest request) {
        UpdateCustomerCommand command = new UpdateCustomerCommand(
                customerId,
                request.familyName(),
                request.givenName()
        );
        UpdateCustomerResult result = updateCustomerUseCase.execute(command);
        CustomerUpdateResponse response = customerWebMapper.toUpdateResponse(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{customerId}/deactivate")
    public ResponseEntity<CustomerDeactivateResponse> deactivate(@PathVariable("customerId") UUID customerId) {
        DeactivateCustomerCommand command = new DeactivateCustomerCommand(customerId);
        DeactivateCustomerResult result = deactivateCustomerUseCase.execute(command);
        CustomerDeactivateResponse response = customerWebMapper.toDeactivateResponse(result);
        return ResponseEntity.ok(response);
    }
}