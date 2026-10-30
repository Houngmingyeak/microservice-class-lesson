//package kh.edu.istad.platform.order.persistence.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.*;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Entity
//@Table(name = "business_products")
//public class BusinessProductEntity {
//
//    @Id
//    @Column(name = "product_id")
//    private UUID productId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "business_id", nullable = false)
//    private BusinessEntity business;
//
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @Column(name = "price", nullable = false, precision = 19, scale = 2)
//    private BigDecimal price;
//}
