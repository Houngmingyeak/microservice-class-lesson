package kh.edu.istad.platform.order.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table("customers") // ប្រើ org.springframework.data.relational.core.mapping.Table
public class CustomerEntity {

    @Id // ប្រើ org.springframework.data.annotation.Id
    @Column("customer_id") // ប្រើ org.springframework.data.relational.core.mapping.Column
    private UUID customerId;

    @Column("username")
    private String username;

    @Column("family_name")
    private String familyName;

    @Column("given_name")
    private String givenName;
}