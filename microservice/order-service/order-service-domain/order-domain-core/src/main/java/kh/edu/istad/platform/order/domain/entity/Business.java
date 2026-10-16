package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.platform.order.domain.valueobject.BusinessId;
import kh.edu.istad.platform.order.domain.valueobject.ProductId;

import java.util.List;

public class Business extends AggregateRoot<BusinessId> {

    private final List<Product> products;
    private final boolean active;

    public Business(BusinessId businessId, List<Product> products, boolean active) {
        super.setId(businessId);
        this.products = products;
        this.active = active;
    }

    public Product findProduct(ProductId productId) {
        if (products == null) {
            return null;
        }
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }
}
