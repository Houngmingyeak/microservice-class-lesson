package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.platform.order.domain.valueobject.OrderId;
import kh.edu.istad.platform.order.domain.valueobject.OrderItemId;

import java.math.BigDecimal;

public class OrderItem extends BaseEntity<OrderItemId> {

    private OrderId orderId;
    private final Product product;
    private final Integer quantity;
    private final Money price;
    private final Money subTotal;



    // used when a new order item is created (id and orderId are set later by the Order)
    public OrderItem(Product product, Integer quantity, Money price, Money subTotal) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    // used when an order item is loaded from the database
    public OrderItem(OrderItemId orderItemId, OrderId orderId, Product product, Integer quantity, Money price, Money subTotal) {
        super.setId(orderItemId);
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }

    private OrderItem(Builder builder) {
        super.setId(builder.id);
        orderId = builder.orderId;
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subTotal;
    }

    void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }

    boolean isPriceValid() {
        if (product == null || product.getPrice() == null || price == null || subTotal == null
                || quantity == null || quantity <= 0) {
            return false;
        }
        if (price.amount() == null || subTotal.amount() == null || product.getPrice().amount() == null) {
            return false;
        }
        return price.amount().compareTo(BigDecimal.ZERO) > 0
                && price.amount().compareTo(product.getPrice().amount()) == 0
                && price.amount().multiply(BigDecimal.valueOf(quantity)).compareTo(subTotal.amount()) == 0;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }

    public static final class Builder {
        private OrderItemId id;
        private OrderId orderId;
        private Product product;
        private Integer quantity;
        private Money price;
        private Money subTotal;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(OrderItemId val) {
            id = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subTotal(Money val) {
            subTotal = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
