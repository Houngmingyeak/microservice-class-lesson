package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.platform.customer.persistence.document.SegmentMembershipDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SegmentMembershipMongoRepository extends ReactiveMongoRepository<SegmentMembershipDocument, String> {
    Mono<Boolean> existsBySegmentIdAndCustomerId(String segmentId, String customerId);
    Flux<SegmentMembershipDocument> findBySegmentId(String segmentId);
    Mono<Long> countBySegmentId(String segmentId);
    Mono<Long> deleteBySegmentIdAndCustomerId(String segmentId, String customerId);
}
