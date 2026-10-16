package kh.edu.istad.platform.order.domain.service;

import kh.edu.istad.platform.order.domain.entity.Business;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.event.OrderCancelledEvent;
import kh.edu.istad.platform.order.domain.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.domain.event.OrderPaidEvent;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Business business) {
        order.validateOrder();

        if (business == null) {
            throw new OrderDomainException("Business must not be null");
        }
        if (!business.isActive()) {
            throw new OrderDomainException("Business with id " + business.getId().id() + " is currently not active!");
        }
        for (OrderItem item : order.getItems()) {
            Product businessProduct = business.findProduct(item.getProduct().getId());
            if (businessProduct == null) {
                throw new OrderDomainException("Product with id " + item.getProduct().getId().id()
                        + " does not belong to this business");
            }
            if (businessProduct.getPrice().amount().compareTo(item.getPrice().amount()) != 0) {
                throw new OrderDomainException("Price of product " + businessProduct.getName()
                        + " is not the current business price");
            }
        }

        order.initializeOrder();
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
    }

}
