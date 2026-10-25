// CreditEntryEntity.java
package kh.edu.istad.platform.payment.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "credit_entry")
public class CreditEntryEntity {

    @Id
    @Column(name = "credit_entry_id")
    private UUID creditEntryId;

    @Column(name = "customer_id", nullable = false, unique = true)
    private UUID customerId;

    @Column(name = "total_credit_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalCreditAmount;
}