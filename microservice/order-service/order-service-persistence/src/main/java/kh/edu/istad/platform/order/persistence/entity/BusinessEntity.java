package kh.edu.istad.platform.order.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "businesses")
public class BusinessEntity {

    @Id
    @Column(name = "business_id")
    private UUID businessId;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "business")
    private List<BusinessProductEntity> products = new ArrayList<>();
}
