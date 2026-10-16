package kh.edu.istad.platform.business.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.business.domain.usecase.RegisterBusinessUseCase;
import kh.edu.istad.platform.business.restapi.dto.RegisterBusinessRequest;
import kh.edu.istad.platform.business.restapi.dto.RegisterBusinessResponse;
import kh.edu.istad.platform.business.restapi.mapper.BusinessWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final RegisterBusinessUseCase registerBusinessUseCase;
    private final BusinessWebMapper businessWebMapper;

    public BusinessController(RegisterBusinessUseCase registerBusinessUseCase,
                              BusinessWebMapper businessWebMapper) {
        this.registerBusinessUseCase = registerBusinessUseCase;
        this.businessWebMapper = businessWebMapper;
    }

    @PostMapping
    public Mono<ResponseEntity<RegisterBusinessResponse>> registerBusiness(
            @Valid @RequestBody RegisterBusinessRequest request) {
        RegisterBusinessCommand command = businessWebMapper.toCommand(request);
        return registerBusinessUseCase.execute(command)
                .map(result -> {
                    RegisterBusinessResponse response = businessWebMapper.toResponse(result);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                });
    }

}
