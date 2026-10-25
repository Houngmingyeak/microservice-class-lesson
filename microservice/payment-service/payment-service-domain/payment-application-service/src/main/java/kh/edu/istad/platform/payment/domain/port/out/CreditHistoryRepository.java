// CreditHistoryRepository.java
package kh.edu.istad.platform.payment.domain.port.out;

import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;

import java.util.List;

public interface CreditHistoryRepository {
    void saveAll(List<CreditHistory> creditHistories);
    List<CreditHistory> findByCustomerId(CustomerId customerId);
}