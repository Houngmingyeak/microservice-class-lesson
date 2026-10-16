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
}
