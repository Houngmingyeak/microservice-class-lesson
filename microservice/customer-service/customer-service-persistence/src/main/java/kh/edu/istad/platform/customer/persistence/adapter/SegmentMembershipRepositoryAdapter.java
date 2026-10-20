package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.platform.customer.domain.port.out.SegmentMembershipRepository;
import kh.edu.istad.platform.customer.persistence.document.SegmentMembershipDocument;
import kh.edu.istad.platform.customer.persistence.repository.SegmentMembershipMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class SegmentMembershipRepositoryAdapter implements SegmentMembershipRepository {

    private final SegmentMembershipMongoRepository repo;

    public SegmentMembershipRepositoryAdapter(SegmentMembershipMongoRepository repo) { this.repo = repo; }

    @Override public Mono<Boolean> exists(String segmentId, String customerId) {
        return repo.existsBySegmentIdAndCustomerId(segmentId, customerId);
    }
    @Override public Mono<Void> add(String segmentId, String customerId) {
        return repo.save(new SegmentMembershipDocument(null, segmentId, customerId, Instant.now())).then();
    }
    @Override public Mono<Boolean> remove(String segmentId, String customerId) {
        return repo.deleteBySegmentIdAndCustomerId(segmentId, customerId).map(n -> n > 0);
    }
    @Override public Flux<String> findCustomerIds(String segmentId) {
        return repo.findBySegmentId(segmentId).map(SegmentMembershipDocument::customerId);
    }
    @Override public Mono<Long> count(String segmentId) { return repo.countBySegmentId(segmentId); }
}
