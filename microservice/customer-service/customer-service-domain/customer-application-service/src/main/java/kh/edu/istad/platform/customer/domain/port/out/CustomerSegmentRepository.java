package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerSegmentRepository {
    Mono<CustomerSegment> save(CustomerSegment segment);
    Mono<CustomerSegment> findById(String id);
    /** status / type may be null (= no filter). */
    Flux<CustomerSegment> findAll(SegmentStatus status, SegmentType type);
    Mono<Boolean> existsByCode(String code);
    /** Case-insensitive; excludeId may be null. */
    Mono<Boolean> existsByName(String name, String excludeId);
    Mono<Void> deleteById(String id);
}
