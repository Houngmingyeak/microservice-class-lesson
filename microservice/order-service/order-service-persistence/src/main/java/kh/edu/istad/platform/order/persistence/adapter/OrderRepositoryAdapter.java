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
import kh.edu.istad.platform.order.persistence.repository.OrderItemJpaRepository;
import kh.edu.istad.platform.order.persistence.repository.OrderJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    // R2DBC មិនមាន Cascade ដូច JPA ទេ ដូច្នេះយើងត្រូវការ OrderItemJpaRepository
    // ដើម្បី save និង query Order Items ដាច់ដោយឡែក
    private final OrderItemJpaRepository orderItemJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository,
                                  OrderItemJpaRepository orderItemJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
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

        // Failure messages រក្សាទុកជា String ផ្តាច់គ្នាដោយ ";"
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

        /*
         * ចំណុចសំខាន់ទី ១ (Save Order):
         * នៅក្នុង R2DBC, method save() ផ្តល់តម្លៃជា Mono<OrderEntity>។
         * ប្រសិនបើយើងមិនហៅ .block() ទេ Stream នឹងមិន subscribe ហើយទិន្នន័យនឹងមិនត្រូវបាន insert ចូល Database ឡើយ។
         */
        orderJpaRepository.save(orderEntity).block();

        /*
         * ចំណុចសំខាន់ទី ២ (Save Order Items):
         * ដោយសារ R2DBC គ្មាន CascadeType.ALL ដូច JPA ទេ យើងត្រូវ Map Order Items
         * ហើយ save ពួកវាចូល Database ដោយផ្ទាល់តាមរយៈ orderItemJpaRepository។
         */
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            List<OrderItemEntity> itemEntities = new ArrayList<>();
            for (OrderItem item : order.getItems()) {
                OrderItemEntity itemEntity = new OrderItemEntity();
                itemEntity.setOrderItemId(item.getId().id());
                // នៅក្នុង R2DBC យើងរក្សាទុកត្រឹម orderId (Foreign Key) មិនមែន object OrderEntity ទាំងមូលទេ
                itemEntity.setOrderId(orderEntity.getOrderId());
                itemEntity.setProductId(item.getProduct().getId().id());
                itemEntity.setProductName(item.getProduct().getName());
                itemEntity.setQuantity(item.getQuantity());
                itemEntity.setPrice(item.getPrice().amount());
                itemEntity.setSubTotal(item.getSubTotal().amount());
                itemEntities.add(itemEntity);
            }

            // saveAll() ត្រឡប់ជា Flux<OrderItemEntity> ដូច្នេះយើងប្រើ .collectList().block() ដើម្បីរង់ចាំដំណើរការ save បញ្ចប់
            orderItemJpaRepository.saveAll(itemEntities).collectList().block();
        }

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(OrderId orderId) {
        /*
         * ចំណុចសំខាន់ទី ៣ (Find Order):
         * findById() របស់ R2DBC ផ្តល់ជា Mono<OrderEntity>។
         * យើងប្រើ .blockOptional() ដើម្បីបម្លែងពី Mono មកជា java.util.Optional<OrderEntity>។
         */
        Optional<OrderEntity> optionalEntity = orderJpaRepository.findById(orderId.id()).blockOptional();
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

        /*
         * ចំណុចសំខាន់ទី ៤ (Fetch Items ដោយដៃ):
         * ក្នុង JPA យើងគ្រាន់តែហៅ orderEntity.getItems()។
         * ប៉ុន្តែក្នុង R2DBC យើងត្រូវ query រក item entities តាមរយៈ orderId ផ្ទាល់ពី orderItemJpaRepository។
         */
        List<OrderItemEntity> itemEntities = orderItemJpaRepository.findAllByOrderId(orderEntity.getOrderId())
                .collectList()
                .block();

        List<OrderItem> items = new ArrayList<>();
        if (itemEntities != null) {
            for (OrderItemEntity itemEntity : itemEntities) {
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