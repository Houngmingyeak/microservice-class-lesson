package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.valueobject.BusinessId;
import kh.edu.istad.platform.order.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.domain.valueobject.OrderId;
import kh.edu.istad.platform.order.domain.valueobject.OrderItemId;
import kh.edu.istad.platform.order.domain.valueobject.ProductId;
import kh.edu.istad.platform.order.domain.valueobject.StreetAddress;
import kh.edu.istad.platform.order.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import kh.edu.istad.platform.order.persistence.repository.OrderJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(order.getId().id());
        orderEntity.setCustomerId(order.getCustomerId().id());
        orderEntity.setBusinessId(order.getBusinessId().id());
        orderEntity.setTrackingId(order.getTrackingId().id());
        orderEntity.setPrice(order.getPrice().amount());
        orderEntity.setOrderStatus(order.getOrderStatus());

        // failure messages are stored as one text column separated by ";"
        String failureMessages = null;
        if (order.getFailureMessages() != null && !order.getFailureMessages().isEmpty()) {
            failureMessages = String.join(";", order.getFailureMessages());
        }
        orderEntity.setFailureMessages(failureMessages);

        StreetAddress address = order.getDeliveryAddress();
        orderEntity.setAddressId(address.id());
        orderEntity.setStreet(address.street());
        orderEntity.setPostalCode(address.postalCode());
        orderEntity.setCity(address.city());

        List<OrderItemEntity> itemEntities = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setOrderItemId(item.getId().id());
            itemEntity.setOrder(orderEntity);
            itemEntity.setProductId(item.getProduct().getId().id());
            itemEntity.setProductName(item.getProduct().getName());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setPrice(item.getPrice().amount());
            itemEntity.setSubTotal(item.getSubTotal().amount());
            itemEntities.add(itemEntity);
        }
        orderEntity.setItems(itemEntities);

        orderJpaRepository.save(orderEntity);

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(OrderId orderId) {
        Optional<OrderEntity> optionalEntity = orderJpaRepository.findById(orderId.id());
        if (optionalEntity.isEmpty()) {
            return Optional.empty();
        }
        OrderEntity orderEntity = optionalEntity.get();

        StreetAddress deliveryAddress = new StreetAddress(
                orderEntity.getAddressId(),
                orderEntity.getStreet(),
                orderEntity.getPostalCode(),
                orderEntity.getCity()
        );

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemEntity itemEntity : orderEntity.getItems()) {
            Product product = new Product(
                    new ProductId(itemEntity.getProductId()),
                    itemEntity.getProductName(),
                    new Money(itemEntity.getPrice())
            );
            OrderItem item = new OrderItem(
                    new OrderItemId(itemEntity.getOrderItemId()),
                    new OrderId(orderEntity.getOrderId()),
                    product,
                    itemEntity.getQuantity(),
                    new Money(itemEntity.getPrice()),
                    new Money(itemEntity.getSubTotal())
            );
            items.add(item);
        }

        List<String> failureMessages = new ArrayList<>();
        if (orderEntity.getFailureMessages() != null && !orderEntity.getFailureMessages().isBlank()) {
            for (String message : orderEntity.getFailureMessages().split(";")) {
                failureMessages.add(message);
            }
        }

        Order order = Order.Builder.builder()
                .orderId(new OrderId(orderEntity.getOrderId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .businessId(new BusinessId(orderEntity.getBusinessId()))
                .deliveryAddress(deliveryAddress)
                .price(new Money(orderEntity.getPrice()))
                .items(items)
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(failureMessages)
                .build();

        return Optional.of(order);
    }
}
