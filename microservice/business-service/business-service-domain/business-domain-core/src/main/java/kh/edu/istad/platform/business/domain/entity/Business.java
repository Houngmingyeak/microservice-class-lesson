package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalStatus;
import kh.edu.istad.platform.business.domain.valueobject.ProductId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Business extends AggregateRoot<BusinessId> {

    private final BusinessName businessName;
    private final BusinessContact businessContact;
    private final List<Product> products;
    private OrderApproval orderApproval;
    private boolean active;
    private final OrderDetail orderDetail;

    // domain critical logic

    public void validateBusiness() {
        if (super.getId() != null) {
            throw new BusinessDomainException("Business id must be null before registration");
        }
        if (businessName == null) {
            throw new BusinessDomainException("Business name must not be null");
        }
        if (businessContact == null) {
            throw new BusinessDomainException("Business contact must not be null");
        }
        if (products != null) {
            for (Product product : products) {
                product.validateProduct();
            }
        }
    }

    public void initializeBusiness() {
        super.setId(new BusinessId(UUID.randomUUID()));
        active = true;
        if (products != null) {
            for (Product product : products) {
                product.initializeProduct();
            }
        }
    }

    public void validateOrder(List<String> failureMessages) {
        if (orderDetail == null) {
            failureMessages.add("Order detail is missing");
            return;
        }
        if (!active) {
            failureMessages.add("Business with id " + super.getId().value() + " is not active");
        }
        if (orderDetail.getProducts() == null || orderDetail.getProducts().isEmpty()) {
            failureMessages.add("Order has no products");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Product orderProduct : orderDetail.getProducts()) {
            Product businessProduct = findProduct(orderProduct.getId());
            if (businessProduct == null) {
                failureMessages.add("Product with id " + orderProduct.getId().value()
                        + " does not exist in this business");
                continue;
            }
            if (!businessProduct.isAvailable()) {
                failureMessages.add("Product " + businessProduct.getName() + " is not available");
            } else if (businessProduct.getQuantity() < orderProduct.getQuantity()) {
                failureMessages.add("Not enough stock for product " + businessProduct.getName());
            }
            if (businessProduct.getPrice().amount().compareTo(orderProduct.getPrice().amount()) != 0) {
                failureMessages.add("Price of product " + businessProduct.getName() + " is not valid");
            }
            total = total.add(orderProduct.getPrice().amount()
                    .multiply(BigDecimal.valueOf(orderProduct.getQuantity())));
        }

        if (orderDetail.getTotalAmount() == null || total.compareTo(orderDetail.getTotalAmount().amount()) != 0) {
            failureMessages.add("Total amount of the order is not equal to the sum of its products: " + total);
        }
    }

    public void constructOrderApproval(OrderApprovalStatus approvalStatus) {
        if (orderDetail == null) {
            throw new BusinessDomainException("Order detail must not be null");
        }
        this.orderApproval = new OrderApproval(
                new OrderApprovalId(UUID.randomUUID()),
                super.getId(),
                orderDetail.getId(),
                approvalStatus
        );
    }

    public Product findProduct(ProductId productId) {
        if (products == null || productId == null) {
            return null;
        }
        for (Product product : products) {
            if (productId.equals(product.getId())) {
                return product;
            }
        }
        return null;
    }

    public BusinessName getBusinessName() {
        return businessName;
    }

    public BusinessContact getBusinessContact() {
        return businessContact;
    }

    public List<Product> getProducts() {
        return products;
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public boolean isActive() {
        return active;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    private Business(Builder builder) {
        businessName = builder.businessName;
        businessContact = builder.businessContact;
        products = builder.products != null ? builder.products : new ArrayList<>();
        orderApproval = builder.orderApproval;
        active = builder.active;
        orderDetail = builder.orderDetail;
        if (builder.businessId != null) {
            super.setId(builder.businessId);
        }
    }

    public static final class Builder {
        private BusinessId businessId;
        private BusinessName businessName;
        private BusinessContact businessContact;
        private List<Product> products;
        private OrderApproval orderApproval;
        private boolean active;
        private OrderDetail orderDetail;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder businessId(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder businessName(BusinessName val) {
            businessName = val;
            return this;
        }

        public Builder businessContact(BusinessContact val) {
            businessContact = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Business build() {
            return new Business(this);
        }
    }
}
