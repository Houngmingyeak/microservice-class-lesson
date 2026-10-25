// CreditEntryRepositoryAdapter.java
package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.edu.istad.platform.payment.persistence.repository.CreditEntryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CreditEntryRepositoryAdapter implements CreditEntryRepository {

    private final CreditEntryJpaRepository jpaRepository;
    private final PaymentPersistenceMapper mapper;

    public CreditEntryRepositoryAdapter(CreditEntryJpaRepository jpaRepository, PaymentPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(CreditEntry creditEntry) {
        jpaRepository.save(mapper.toEntity(creditEntry));
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        return jpaRepository.findByCustomerId(customerId.value()).map(mapper::toDomain);
    }
}