package kh.edu.istad.platform.order.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table("order_items")
public class OrderItemEntity {

    @Id
    @Embedded.Nullable
    private OrderItemEntityId id;

    @Column("product_id")
    private UUID productId;

    @Column("product_name")
    private String productName;

    @Column("quantity")
    private Integer quantity;

    @Column("price")
    private BigDecimal price;

    @Column("sub_total")
    private BigDecimal subTotal;
}
