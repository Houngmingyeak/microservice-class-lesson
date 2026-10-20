package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class CustomerReactiveRepositoryAdapter implements CustomerRepository {

    private final CustomerMongoRepository customerMongoRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Customer> save(Customer customer) {
        CustomerDocument document = customerPersistenceMapper.toDocument(customer);
        return customerMongoRepository.save(document)
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Customer> findById(Customerid customerId) {
        return customerMongoRepository.findById(customerId.value())
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Customer> findAll() {
        return customerMongoRepository.findAll()
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Customerid customerId) {
        return customerMongoRepository.deleteById(customerId.value());
    }

    @Override
    public Mono<Customer> update(Customer customer) {
        return customerMongoRepository.findById(customer.getId().value())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "Customer not found: " + customer.getId())))
                .flatMap(existing -> {
                    CustomerDocument updated = customerPersistenceMapper.toDocument(customer);
                    updated.setCreatedAt(existing.getCreatedAt());
                    updated.setCreatedBy(existing.getCreatedBy());
                    return customerMongoRepository.save(updated);
                })
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Customer> findByUsername(String username) {
        return customerMongoRepository.findFirstByUsername(username)
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Customer> query(String username) {
        Query query = Query.query(Criteria.where("username").is(username));
        return reactiveMongoTemplate.find(query, CustomerDocument.class)
                .map(customerPersistenceMapper::toDomain);
    }
}