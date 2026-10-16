package kh.edu.istad.platform.order.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @Column(name = "customer_id")
    private UUID customerId;
}
