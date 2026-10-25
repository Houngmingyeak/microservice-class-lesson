// CreditResponse.java
package kh.edu.istad.platform.payment.restapi.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreditResponse(UUID customerId, BigDecimal totalCreditAmount) {
}