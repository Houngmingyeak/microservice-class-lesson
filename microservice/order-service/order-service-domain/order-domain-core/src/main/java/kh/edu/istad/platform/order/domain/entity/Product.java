package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.platform.order.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {

    private final String name;
    private final Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
