package kh.edu.istad.platform.order.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

/*
 * OrderItemEntity កំណែ R2DBC (ជំនួស JPA)។
 *
 * អ្វីដែលបានដកចេញ (ពី JPA):
 * - jakarta.persistence.Entity, FetchType, JoinColumn, ManyToOne
 *   ព្រោះ R2DBC មិនគាំទ្រ relationship ទេ។
 * - field "OrderEntity order" ត្រូវជំនួសដោយ "UUID orderId" (Foreign Key ធម្មតា)
 *   ដូច្នេះ Lombok នឹងបង្កើត setOrderId(UUID) ដែល OrderRepositoryAdapter ត្រូវការ។
 *
 * អ្វីដែលបានប្តូរ:
 * - @Table  → org.springframework.data.relational.core.mapping.Table
 * - @Column → org.springframework.data.relational.core.mapping.Column
 * - @Id     → org.springframework.data.annotation.Id
 *   (R2DBC @Column មិនមាន nullable, precision, scale ទេ
 *    ការកំណត់ទាំងនោះនៅក្នុង SQL របស់ Liquibase/DDL ហើយ)
 *
 * implements Persistable<UUID>:
 * - ID (orderItemId) ត្រូវបានបង្កើតក្នុង code មិនមែនដោយ Database។
 * - ក្នុង R2DBC បើ @Id មានតម្លៃរួចហើយ save() នឹងគិតថាជា "update" ហើយ
 *   ប្រើ UPDATE ដែលរក row មិនឃើញ → error "Failed to update table [order_items]".
 * - ដូច្នេះយើង implement Persistable ហើយ isNew() = true ដើម្បីបង្ខំឱ្យប្រើ INSERT។
 */
@Getter
@Setter
@NoArgsConstructor
@Table("order_items")
public class OrderItemEntity implements Persistable<UUID> {

    @Id
    @Column("order_item_id")
    private UUID orderItemId;

    // Foreign Key ទៅ table orders (ជំនួស @ManyToOne OrderEntity order)
    @Column("order_id")
    private UUID orderId;

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

    // @Transient = មិនរក្សាទុកក្នុង Database។ ប្រើតែប្រាប់ Spring ថា row នេះថ្មី (INSERT)។
    @Transient
    private boolean newEntity = true;

    /* ---------- Persistable<UUID> ---------- */

    @Override
    public UUID getId() {
        // Spring Data ហៅ method នេះដើម្បីដឹង ID របស់ entity
        return orderItemId;
    }

    @Override
    public boolean isNew() {
        return newEntity;
    }
}