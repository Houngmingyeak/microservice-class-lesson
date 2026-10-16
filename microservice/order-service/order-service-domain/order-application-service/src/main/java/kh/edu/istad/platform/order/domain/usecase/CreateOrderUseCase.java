package kh.edu.istad.platform.order.domain.usecase;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.OrderItemCommand;
import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;
import kh.edu.istad.platform.order.domain.exception.OrderNotFoundException;
import kh.edu.istad.platform.order.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.order.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.service.OrderDomainService;
import kh.edu.istad.platform.order.domain.valueobject.BusinessId;
import kh.edu.istad.platform.order.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.domain.valueobject.ProductId;
import kh.edu.istad.platform.order.domain.valueobject.StreetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;

    public CreateOrderResult execute(CreateOrderCommand command) {
        log.info("create order usecase: {}", command);

        if (command.items() == null || command.items().isEmpty()) {
            throw new OrderDomainException("Order must have at least one item");
        }

        // validate by loading data from persistence (output ports)
        CustomerId customerId = new CustomerId(command.customerId());
        customerRepository.findCustomer(customerId)
                .orElseThrow(() -> new OrderNotFoundException("Could not find customer with id: " + command.customerId()));

        BusinessId businessId = new BusinessId(command.businessId());
        Business business = businessRepository.findBusinessInformation(businessId)
                .orElseThrow(() -> new OrderNotFoundException("Could not find business with id: " + command.businessId()));

        // build the delivery address value object
        StreetAddress deliveryAddress = new StreetAddress(
                UUID.randomUUID(),
                command.street(),
                command.postalCode(),
                command.city()
        );

        // build every order item from the command
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemCommand itemCommand : command.items()) {
            Product product = new Product(
                    new ProductId(itemCommand.productId()),
                    itemCommand.productName(),
                    new Money(itemCommand.price())
            );
            OrderItem orderItem = new OrderItem(
                    product,
                    itemCommand.quantity(),
                    new Money(itemCommand.price()),
                    new Money(itemCommand.subTotal())
            );
            items.add(orderItem);
        }

        // build the domain object (id, tracking id and status stay null on purpose)
        Order order = Order.Builder.builder()
                .customerId(customerId)
                .businessId(businessId)
                .deliveryAddress(deliveryAddress)
                .price(new Money(command.price()))
                .items(items)
                .build();

        // invoke domain logic: validates the order and business, generates ids, sets status PENDING
        orderDomainService.validateAndInitiateOrder(order, business);

        // save data into the database (output port)
        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResult(
                savedOrder.getId().id(),
                savedOrder.getTrackingId().id(),
                savedOrder.getOrderStatus().name(),
                "Order created successfully"
        );
    }

}
