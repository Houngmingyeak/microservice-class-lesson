package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerSegmentMongoRepository extends ReactiveMongoRepository<CustomerSegmentDocument, String> {
    Mono<Boolean> existsByCode(String code);
    Mono<Boolean> existsByNameLower(String nameLower);
    Mono<Boolean> existsByNameLowerAndIdNot(String nameLower, String id);
}
