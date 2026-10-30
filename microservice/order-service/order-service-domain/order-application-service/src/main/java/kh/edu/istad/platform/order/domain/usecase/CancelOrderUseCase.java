//package kh.edu.istad.platform.order.domain.usecase;
//
//import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
//import kh.edu.istad.platform.order.domain.entity.Order;
//import kh.edu.istad.platform.order.domain.exception.OrderNotFoundException;
//import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
//import kh.edu.istad.platform.order.domain.service.OrderDomainService;
//import kh.edu.istad.platform.order.domain.valueobject.OrderId;
//import kh.edu.istad.platform.order.domain.valueobject.OrderStatus;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class CancelOrderUseCase {
//
//    private final OrderDomainService orderDomainService;
//    private final OrderRepository orderRepository;
//
//    public CreateOrderResult execute(UUID orderId, List<String> failureMessages) {
//        log.info("cancel order usecase: {}", orderId);
//
//        Order order = orderRepository.findById(new OrderId(orderId))
//                .orElseThrow(() -> new OrderNotFoundException("Could not find order with id: " + orderId));
//
//        if (order.getOrderStatus() == OrderStatus.PAID) {
//            // a paid order goes PAID -> CANCELLING -> CANCELLED
//            orderDomainService.cancelOrderPayment(order, failureMessages);
//            orderDomainService.cancelOrder(order, List.of());
//        } else {
//            // a pending order goes PENDING -> CANCELLED (throws for any other status)
//            orderDomainService.cancelOrder(order, failureMessages);
//        }
//
//        Order savedOrder = orderRepository.save(order);
//
//        return new CreateOrderResult(
//                savedOrder.getId().id(),
//                savedOrder.getTrackingId().id(),
//                savedOrder.getOrderStatus().name(),
//                "Order cancelled successfully"
//        );
//    }
//
//}
