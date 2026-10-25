// CreditController.java
package kh.edu.istad.platform.payment.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.payment.domain.usecase.AddCreditUseCase;
import kh.edu.istad.platform.payment.restapi.dto.AddCreditRequest;
import kh.edu.istad.platform.payment.restapi.dto.CreditResponse;
import kh.edu.istad.platform.payment.restapi.mapper.PaymentWebMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credits")
public class CreditController {

    private final AddCreditUseCase addCreditUseCase;
    private final PaymentWebMapper mapper;

    public CreditController(AddCreditUseCase addCreditUseCase, PaymentWebMapper mapper) {
        this.addCreditUseCase = addCreditUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CreditResponse> addCredit(@Valid @RequestBody AddCreditRequest request) {
        var result = addCreditUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.ok(mapper.toResponse(result));
    }
}