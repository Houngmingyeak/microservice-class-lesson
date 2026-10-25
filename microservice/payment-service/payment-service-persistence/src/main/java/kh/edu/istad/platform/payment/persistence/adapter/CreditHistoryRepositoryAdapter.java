// CreditHistoryRepositoryAdapter.java
package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.CreditHistoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CreditHistoryRepositoryAdapter implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository jpaRepository;
    private final PaymentPersistenceMapper mapper;

    public CreditHistoryRepositoryAdapter(CreditHistoryJpaRepository jpaRepository, PaymentPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void saveAll(List<CreditHistory> creditHistories) {
        jpaRepository.saveAll(creditHistories.stream().map(mapper::toEntity).toList());
    }

    @Override
    public List<CreditHistory> findByCustomerId(CustomerId customerId) {
        return jpaRepository.findByCustomerId(customerId.value()).stream()
                .map(mapper::toDomain)
                .toList();
    }
}