package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.customer.domain.usecase.CustomerSegmentUseCase;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;
import kh.edu.istad.platform.customer.restapi.dto.SegmentDtos.*;
import kh.edu.istad.platform.customer.restapi.mapper.SegmentWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/segments")
public class CustomerSegmentController {

    private final CustomerSegmentUseCase useCase;
    private final SegmentWebMapper mapper;

    public CustomerSegmentController(CustomerSegmentUseCase useCase, SegmentWebMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SegmentResponse> create(@Valid @RequestBody CreateSegmentRequest req) {
        return useCase.create(mapper.toCommand(req)).map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<SegmentResponse> get(@PathVariable String id) {
        return useCase.get(id).map(mapper::toResponse);
    }

    @GetMapping
    public Flux<SegmentResponse> list(@RequestParam(required = false) SegmentStatus status,
                                      @RequestParam(required = false) SegmentType type) {
        return useCase.list(status, type).map(mapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<SegmentResponse> update(@PathVariable String id, @Valid @RequestBody UpdateSegmentRequest req) {
        return useCase.update(mapper.toCommand(id, req)).map(mapper::toResponse);
    }

    @PatchMapping("/{id}/activate")
    public Mono<SegmentResponse> activate(@PathVariable String id) {
        return useCase.activate(id).map(mapper::toResponse);
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<SegmentResponse> deactivate(@PathVariable String id) {
        return useCase.deactivate(id).map(mapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return useCase.delete(id);
    }

    @GetMapping("/{id}/customers")
    public Flux<String> members(@PathVariable String id) {
        return useCase.listMembers(id);
    }

    @PostMapping("/{id}/customers/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> assign(@PathVariable String id, @PathVariable String customerId) {
        return useCase.assignCustomer(id, customerId);
    }

    @DeleteMapping("/{id}/customers/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> remove(@PathVariable String id, @PathVariable String customerId) {
        return useCase.removeCustomer(id, customerId);
    }
}
