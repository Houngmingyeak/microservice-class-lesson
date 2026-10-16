package kh.edu.istad.platform.order.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.usecase.CancelOrderUseCase;
import kh.edu.istad.platform.order.domain.usecase.CreateOrderUseCase;
import kh.edu.istad.platform.order.domain.usecase.PayOrderUseCase;
import kh.edu.istad.platform.order.restapi.dto.CancelOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderResponse;
import kh.edu.istad.platform.order.restapi.mapper.OrderWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final OrderWebMapper orderWebMapper;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           PayOrderUseCase payOrderUseCase,
                           CancelOrderUseCase cancelOrderUseCase,
                           OrderWebMapper orderWebMapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.payOrderUseCase = payOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.orderWebMapper = orderWebMapper;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        CreateOrderCommand command = orderWebMapper.toCommand(request);
        CreateOrderResult result = createOrderUseCase.execute(command);
        CreateOrderResponse response = orderWebMapper.toResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{orderId}/pay")
    public ResponseEntity<CreateOrderResponse> payOrder(@PathVariable("orderId") UUID orderId) {
        CreateOrderResult result = payOrderUseCase.execute(orderId);
        CreateOrderResponse response = orderWebMapper.toResponse(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<CreateOrderResponse> cancelOrder(@PathVariable("orderId") UUID orderId,
                                                           @RequestBody(required = false) CancelOrderRequest request) {
        List<String> failureMessages = List.of();
        if (request != null && request.failureMessages() != null) {
            failureMessages = request.failureMessages();
        }
        CreateOrderResult result = cancelOrderUseCase.execute(orderId, failureMessages);
        CreateOrderResponse response = orderWebMapper.toResponse(result);
        return ResponseEntity.ok(response);
    }

}
