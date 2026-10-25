// PaymentController.java
package kh.edu.istad.platform.payment.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.payment.domain.dto.CancelPaymentCommand;
import kh.edu.istad.platform.payment.domain.usecase.CancelPaymentUseCase;
import kh.edu.istad.platform.payment.domain.usecase.GetPaymentUseCase;
import kh.edu.istad.platform.payment.domain.usecase.InitiatePaymentUseCase;
import kh.edu.istad.platform.payment.restapi.dto.InitiatePaymentRequest;
import kh.edu.istad.platform.payment.restapi.dto.PaymentResponse;
import kh.edu.istad.platform.payment.restapi.mapper.PaymentWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final InitiatePaymentUseCase initiatePaymentUseCase;
    private final CancelPaymentUseCase cancelPaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    private final PaymentWebMapper mapper;

    public PaymentController(InitiatePaymentUseCase initiatePaymentUseCase,
                             CancelPaymentUseCase cancelPaymentUseCase,
                             GetPaymentUseCase getPaymentUseCase,
                             PaymentWebMapper mapper) {
        this.initiatePaymentUseCase = initiatePaymentUseCase;
        this.cancelPaymentUseCase = cancelPaymentUseCase;
        this.getPaymentUseCase = getPaymentUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody InitiatePaymentRequest request) {
        var result = initiatePaymentUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity<PaymentResponse> cancel(@PathVariable UUID orderId) {
        var result = cancelPaymentUseCase.execute(new CancelPaymentCommand(orderId));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(mapper.toResponse(getPaymentUseCase.execute(orderId)));
    }
}