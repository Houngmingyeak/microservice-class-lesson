package kh.edu.istad.platform.order.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kh.edu.istad.platform.order.domain.valueobject.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "business_id", nullable = false)
    private UUID businessId;

    @Column(name = "tracking_id")
    private UUID trackingId;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "failure_messages", length = 2000)
    private String failureMessages;

    @Column(name = "address_id")
    private UUID addressId;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "city", nullable = false)
    private String city;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();
}
