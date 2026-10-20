package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.Customerid;
import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.dto.PageResult;
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
import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.dto.PageResult;
import kh.edu.istad.platform.customer.persistence.query.DynamicQuery;
import kh.edu.istad.platform.customer.persistence.query.ReactiveDynamicSearcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class CustomerReactiveRepositoryAdapter implements CustomerRepository {

    private final CustomerMongoRepository customerMongoRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final ReactiveDynamicSearcher dynamicSearcher;


    private static final Set<String> SORTABLE_FIELDS =
            Set.of("username", "familyName", "givenName", "email", "status", "createdAt");

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

    @Override
    public Mono<PageResult<Customer>> search(CustomerFilter filter) {
        String sortBy = SORTABLE_FIELDS.contains(filter.sortBy()) ? filter.sortBy() : "createdAt";
        Sort.Direction direction = "asc".equalsIgnoreCase(filter.sortDir())
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(filter.page(), filter.size(), Sort.by(direction, sortBy));

        DynamicQuery query = DynamicQuery.create()
                .anyContains(filter.keyword(), "username", "familyName", "givenName", "email")
                .contains("username", filter.username())
                .contains("familyName", filter.familyName())
                .contains("givenName", filter.givenName())
                .contains("email", filter.email())
                .contains("phoneNumber", filter.phoneNumber())
                .eq("status", filter.status())
                .from("createdAt", filter.createdFrom())
                .to("createdAt", filter.createdTo());

        return dynamicSearcher.search(query, pageable, CustomerDocument.class)
                .map(page -> new PageResult<>(
                        page.getContent().stream()
                                .map(customerPersistenceMapper::toDomain)
                                .toList(),
                        page.getTotalElements(),
                        filter.page(),
                        filter.size()));
    }
}