package kh.edu.istad.platform.business.persistence.document;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductDocument {

    private String productId;
    private String name;
    private BigDecimal price;
    private int quantity;
    private boolean available;
}
