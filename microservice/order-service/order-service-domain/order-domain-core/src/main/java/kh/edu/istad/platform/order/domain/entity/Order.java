package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;
import kh.edu.istad.platform.order.domain.valueobject.BusinessId;
import kh.edu.istad.platform.order.domain.valueobject.CustomerId;
import kh.edu.istad.platform.order.domain.valueobject.OrderId;
import kh.edu.istad.platform.order.domain.valueobject.OrderItemId;
import kh.edu.istad.platform.order.domain.valueobject.OrderStatus;
import kh.edu.istad.platform.order.domain.valueobject.StreetAddress;
import kh.edu.istad.platform.order.domain.valueobject.TrackingId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final BusinessId businessId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;
    private Long version;



    // domain critical logic

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void initializeOrder() {
        super.setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> messages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }
        updateFailureMessages(messages);
        orderStatus = OrderStatus.CANCELLING;
    }

    public void cancel(List<String> messages) {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }
        updateFailureMessages(messages);
        orderStatus = OrderStatus.CANCELLED;
    }

    private void updateFailureMessages(List<String> messages) {
        if (messages == null) {
            return;
        }
        if (failureMessages == null) {
            failureMessages = new ArrayList<>();
        }
        for (String message : messages) {
            if (message != null && !message.isEmpty()) {
                failureMessages.add(message);
            }
        }
    }

    private void validateInitialOrder() {
        if (customerId == null) {
            throw new OrderDomainException("Customer id must not be null");
        }
        if (businessId == null) {
            throw new OrderDomainException("Business id must not be null");
        }
        if (deliveryAddress == null) {
            throw new OrderDomainException("Delivery address must not be null");
        }
        if (items == null || items.isEmpty()) {
            throw new OrderDomainException("Order must have at least one item");
        }
        if (orderStatus != null || super.getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void validateTotalPrice() {
        if (price == null || price.amount() == null || price.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateItemsPrice() {
        BigDecimal itemsTotal = BigDecimal.ZERO;
        for (OrderItem item : items) {
            if (!item.isPriceValid()) {
                throw new OrderDomainException("Order item price is not valid for product: "
                        + (item.getProduct() != null ? item.getProduct().getName() : "unknown"));
            }
            itemsTotal = itemsTotal.add(item.getSubTotal().amount());
        }
        if (price.amount().compareTo(itemsTotal) != 0) {
            throw new OrderDomainException("Total price: " + price.amount()
                    + " is not equal to order items total: " + itemsTotal + "!");
        }
    }

    private void initializeOrderItems() {
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(), new OrderItemId(UUID.randomUUID()));
        }
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public BusinessId getBusinessId() {
        return businessId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    private Order(Builder builder) {
        // orderId is used when rehydrating an order from persistence; id remains
        // as a compatibility alias for callers that build new orders directly.
        super.setId(builder.orderId != null ? builder.orderId : builder.id);
        customerId = builder.customerId;
        businessId = builder.businessId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
        version = builder.version;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private BusinessId businessId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;
        private Long version;
        private OrderId id;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder businessId(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Builder version(Long val) {
            version = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }

        public Builder id(OrderId val) {
            id = val;
            return this;
        }
    }

}
