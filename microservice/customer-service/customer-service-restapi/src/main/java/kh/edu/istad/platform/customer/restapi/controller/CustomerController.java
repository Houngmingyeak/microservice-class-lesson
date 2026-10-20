package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.usecase.DeactivateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.InitiateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.QueryRepositoryUseCase;
import kh.edu.istad.platform.customer.domain.usecase.UpdateCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.*;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.restapi.dto.PageResponse;
import java.time.Instant;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final InitiateCustomerUseCase initiateCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;
    private final QueryRepositoryUseCase queryRepositoryUseCase;
    private final CustomerWebMapper customerWebMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<CustomerInitiateResponse> initiateCustomer(
            @Valid @RequestBody CustomerInitiateRequest request
    ) {
        return initiateCustomerUseCase
                .execute(customerWebMapper.toCommand(request))
                .map(customerWebMapper::toResponse);
    }

    @GetMapping
    public Flux<CustomerInitiateResponse> findAll() {
        return queryRepositoryUseCase.findAll()
                .map(customerWebMapper::customerToResponse);
    }

    @GetMapping("/username/{username}")
    public Mono<CustomerInitiateResponse> findByUsername(@PathVariable("username") String username) {
        return queryRepositoryUseCase.findByUsername(username)
                .map(customerWebMapper::customerToResponse);
    }

    @PutMapping("/{id}")
    public Mono<CustomerUpdateResponse> updateCustomer(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        return updateCustomerUseCase
                .execute(customerWebMapper.toUpdateCommand(id, request))
                .map(customerWebMapper::toUpdateResponse);
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<CustomerDeactivateResponse> deactivateCustomer(@PathVariable("id") UUID id) {
        return deactivateCustomerUseCase
                .execute(new Customerid(id))
                .map(customerWebMapper::toDeactivateResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable("id") UUID id) {
        return queryRepositoryUseCase.deleteById(new Customerid(id));
    }

    // SEARCH (dynamic filter + paging + sort)
    @GetMapping("/search")
    public Mono<PageResponse<CustomerInitiateResponse>> search(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "familyName", required = false) String familyName,
            @RequestParam(name = "givenName", required = false) String givenName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "status", required = false) CustomerStatus status,
            @RequestParam(name = "createdFrom", required = false) Instant createdFrom,
            @RequestParam(name = "createdTo", required = false) Instant createdTo,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir
    ) {
        CustomerFilter filter = new CustomerFilter(
                keyword, username, familyName, givenName, email, phoneNumber,
                status, createdFrom, createdTo, page, size, sortBy, sortDir);

        return queryRepositoryUseCase.search(filter)
                .map(result -> PageResponse.from(result, c -> customerWebMapper.customerToResponse(c)));
    }
}