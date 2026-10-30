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
@Table("businesses")
public class BusinessEntity {

    @Id
    @Embedded.Nullable
    private BusinessEntityId businessId;

    @Column("business_active")
    private boolean businessActive;
    @Column("product_name")
    private String productName;
    @Column("product_price")
    private BigDecimal productPrice;

}
