package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.platform.order.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {

    private String name;
    private Money price;

    private Product(Builder builder) {
        super.setId(builder.id);
        name = builder.name;
        price = builder.price;
    }

    public void updateComfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }



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

    public static final class Builder {
        private ProductId id;
        private String name;
        private Money price;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(ProductId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
