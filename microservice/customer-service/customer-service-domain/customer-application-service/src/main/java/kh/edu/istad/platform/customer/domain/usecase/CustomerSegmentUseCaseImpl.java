package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.SegmentCommands.CreateSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.SegmentCommands.UpdateSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.SegmentResult;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.exception.CustomerSegmentDomainException;
import kh.edu.istad.platform.customer.domain.exception.SegmentErrorCode;
import kh.edu.istad.platform.customer.domain.port.out.CustomerExistencePort;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import kh.edu.istad.platform.customer.domain.port.out.SegmentMembershipRepository;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static kh.edu.istad.platform.customer.domain.exception.SegmentErrorCode.*;

public class CustomerSegmentUseCaseImpl implements CustomerSegmentUseCase {

    private final CustomerSegmentRepository segments;
    private final SegmentMembershipRepository memberships;
    private final CustomerExistencePort customers;

    public CustomerSegmentUseCaseImpl(CustomerSegmentRepository segments,
                                      SegmentMembershipRepository memberships,
                                      CustomerExistencePort customers) {
        this.segments = segments;
        this.memberships = memberships;
        this.customers = customers;
    }

    @Override
    public Mono<SegmentResult> create(CreateSegmentCommand c) {
        return Mono.fromSupplier(() -> CustomerSegment.create(c.code(), c.name(), c.description(), c.type(), c.criteria()))
                .flatMap(seg -> segments.existsByCode(seg.getCode())
                        .flatMap(dup -> dup ? Mono.<Boolean>error(err(SEGMENT_DUPLICATE, "Segment code already exists: " + seg.getCode()))
                                : segments.existsByName(seg.getName(), null))
                        .flatMap(dup -> dup ? Mono.<CustomerSegment>error(err(SEGMENT_DUPLICATE, "Segment name already exists: " + seg.getName()))
                                : segments.save(seg)))
                .map(saved -> SegmentResult.from(saved, 0));
    }

    @Override
    public Mono<SegmentResult> get(String id) {
        return load(id).flatMap(this::toResult);
    }

    @Override
    public Flux<SegmentResult> list(SegmentStatus status, SegmentType type) {
        return segments.findAll(status, type).concatMap(this::toResult);
    }

    @Override
    public Mono<SegmentResult> update(UpdateSegmentCommand c) {
        return load(c.segmentId()).flatMap(seg -> {
            seg.update(c.name(), c.description(), c.criteria());
            return segments.existsByName(seg.getName(), seg.getId())
                    .flatMap(dup -> dup ? Mono.<CustomerSegment>error(err(SEGMENT_DUPLICATE, "Segment name already exists: " + seg.getName()))
                            : segments.save(seg));
        }).flatMap(this::toResult);
    }

    @Override
    public Mono<SegmentResult> activate(String id) {
        return load(id).flatMap(seg -> { seg.activate(); return segments.save(seg); }).flatMap(this::toResult);
    }

    @Override
    public Mono<SegmentResult> deactivate(String id) {
        return load(id).flatMap(seg -> { seg.deactivate(); return segments.save(seg); }).flatMap(this::toResult);
    }

    /** Segments with members cannot be deleted - deactivate them instead. */
    @Override
    public Mono<Void> delete(String id) {
        return load(id).flatMap(seg -> memberships.count(seg.getId())
                .flatMap(n -> n > 0
                        ? Mono.<Void>error(err(SEGMENT_NOT_EMPTY, "Segment has " + n + " member(s); remove them or deactivate the segment"))
                        : segments.deleteById(seg.getId())));
    }

    @Override
    public Mono<Void> assignCustomer(String segmentId, String customerId) {
        return load(segmentId).flatMap(seg -> {
            seg.assertManualMembershipAllowed();
            return customers.exists(customerId)
                    .flatMap(found -> found ? memberships.exists(segmentId, customerId)
                            : Mono.<Boolean>error(err(CUSTOMER_NOT_FOUND, "Customer not found: " + customerId)))
                    .flatMap(already -> already ? Mono.<Void>error(err(MEMBERSHIP_CONFLICT, "Customer already in segment"))
                            : memberships.add(segmentId, customerId));
        });
    }

    @Override
    public Mono<Void> removeCustomer(String segmentId, String customerId) {
        return load(segmentId).flatMap(seg -> {
            seg.assertManualMembershipAllowed();
            return memberships.remove(segmentId, customerId)
                    .flatMap(removed -> removed ? Mono.<Void>empty()
                            : Mono.<Void>error(err(MEMBERSHIP_NOT_FOUND, "Customer is not in segment")));
        });
    }

    @Override
    public Flux<String> listMembers(String segmentId) {
        return load(segmentId).flatMapMany(seg -> memberships.findCustomerIds(seg.getId()));
    }

    /* ---------- helpers ---------- */

    private Mono<CustomerSegment> load(String id) {
        return segments.findById(id)
                .switchIfEmpty(Mono.error(err(SEGMENT_NOT_FOUND, "Segment not found: " + id)));
    }

    private Mono<SegmentResult> toResult(CustomerSegment s) {
        return memberships.count(s.getId()).map(n -> SegmentResult.from(s, n));
    }

    private static CustomerSegmentDomainException err(SegmentErrorCode code, String msg) {
        return new CustomerSegmentDomainException(code, msg);
    }
}
