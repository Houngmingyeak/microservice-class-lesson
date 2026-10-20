package kh.edu.istad.platform.customer.domain.port.out;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SegmentMembershipRepository {
    Mono<Boolean> exists(String segmentId, String customerId);
    Mono<Void> add(String segmentId, String customerId);
    /** @return true if a membership was removed */
    Mono<Boolean> remove(String segmentId, String customerId);
    Flux<String> findCustomerIds(String segmentId);
    Mono<Long> count(String segmentId);
}
