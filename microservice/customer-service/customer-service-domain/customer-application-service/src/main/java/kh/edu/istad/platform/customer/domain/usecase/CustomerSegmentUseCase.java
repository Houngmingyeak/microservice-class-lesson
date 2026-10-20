package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.SegmentCommands.CreateSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.SegmentCommands.UpdateSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.SegmentResult;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerSegmentUseCase {
    Mono<SegmentResult> create(CreateSegmentCommand command);
    Mono<SegmentResult> get(String segmentId);
    Flux<SegmentResult> list(SegmentStatus status, SegmentType type);
    Mono<SegmentResult> update(UpdateSegmentCommand command);
    Mono<SegmentResult> activate(String segmentId);
    Mono<SegmentResult> deactivate(String segmentId);
    Mono<Void> delete(String segmentId);

    Mono<Void> assignCustomer(String segmentId, String customerId);
    Mono<Void> removeCustomer(String segmentId, String customerId);
    Flux<String> listMembers(String segmentId);
}
