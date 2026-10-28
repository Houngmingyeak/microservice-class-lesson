
CREATE TABLE orders (
    order_id         UUID PRIMARY KEY,
    customer_id      UUID NOT NULL,
    business_id      UUID NOT NULL,
    tracking_id      UUID,
    price            NUMERIC(19, 2) NOT NULL,
    -- OrderEntity ប្រើ @Enumerated(EnumType.STRING) ដូច្នេះរក្សាទុកជាអក្សរ (ឧ. PENDING, PAID)
    order_status     VARCHAR(50) NOT NULL,
    failure_messages VARCHAR(2000),
    address_id       UUID,
    street           VARCHAR(255) NOT NULL,
    postal_code      VARCHAR(255) NOT NULL,
    city             VARCHAR(255) NOT NULL
);
CREATE TABLE order_items (
    order_item_id UUID PRIMARY KEY,
    order_id      UUID NOT NULL,
    product_id    UUID NOT NULL,
    product_name  VARCHAR(255) NOT NULL,
    quantity      INTEGER NOT NULL,
    price         NUMERIC(19, 2) NOT NULL,
    sub_total     NUMERIC(19, 2) NOT NULL,
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders (order_id)
);
