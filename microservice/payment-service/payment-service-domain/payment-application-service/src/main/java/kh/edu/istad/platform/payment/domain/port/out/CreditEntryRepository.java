// CreditEntryRepository.java
package kh.edu.istad.platform.payment.domain.port.out;

import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;

import java.util.Optional;

public interface CreditEntryRepository {
    void save(CreditEntry creditEntry);
    Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}