package kh.edu.istad.platform.order.domain.usecase;

import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.exception.OrderNotFoundException;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.service.OrderDomainService;
import kh.edu.istad.platform.order.domain.valueobject.OrderId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayOrderUseCase {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;

    public CreateOrderResult execute(UUID orderId) {
        log.info("pay order usecase: {}", orderId);

        Order order = orderRepository.findById(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException("Could not find order with id: " + orderId));

        // throws if the status is not PENDING, then sets PAID
        orderDomainService.payOrder(order);

        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResult(
                savedOrder.getId().id(),
                savedOrder.getTrackingId().id(),
                savedOrder.getOrderStatus().name(),
                "Order paid successfully"
        );
    }

}
