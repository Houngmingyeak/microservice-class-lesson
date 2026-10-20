package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentCriteria;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument.CriteriaDocument;
import kh.edu.istad.platform.customer.persistence.repository.CustomerSegmentMongoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Component
public class CustomerSegmentRepositoryAdapter implements CustomerSegmentRepository {

    private final CustomerSegmentMongoRepository repo;
    private final ReactiveMongoTemplate template;

    public CustomerSegmentRepositoryAdapter(CustomerSegmentMongoRepository repo, ReactiveMongoTemplate template) {
        this.repo = repo;
        this.template = template;
    }

    @Override public Mono<CustomerSegment> save(CustomerSegment s) { return repo.save(toDocument(s)).map(this::toDomain); }
    @Override public Mono<CustomerSegment> findById(String id) { return repo.findById(id).map(this::toDomain); }
    @Override public Mono<Boolean> existsByCode(String code) { return repo.existsByCode(code); }
    @Override public Mono<Void> deleteById(String id) { return repo.deleteById(id); }

    @Override
    public Mono<Boolean> existsByName(String name, String excludeId) {
        String key = lower(name);
        return excludeId == null ? repo.existsByNameLower(key) : repo.existsByNameLowerAndIdNot(key, excludeId);
    }

    @Override
    public Flux<CustomerSegment> findAll(SegmentStatus status, SegmentType type) {
        Query q = new Query().with(Sort.by("name"));
        if (status != null) q.addCriteria(Criteria.where("status").is(status.name()));
        if (type != null) q.addCriteria(Criteria.where("type").is(type.name()));
        return template.find(q, CustomerSegmentDocument.class).map(this::toDomain);
    }

    private CustomerSegmentDocument toDocument(CustomerSegment s) {
        SegmentCriteria c = s.getCriteria();
        CriteriaDocument cd = c == null ? null : new CriteriaDocument(c.minTotalSpend(), c.minOrderCount(), c.country());
        return new CustomerSegmentDocument(s.getId(), s.getCode(), lower(s.getName()), s.getName(), s.getDescription(),
                s.getType().name(), cd, s.getStatus().name(), s.getCreatedAt(), s.getUpdatedAt());
    }

    private CustomerSegment toDomain(CustomerSegmentDocument d) {
        CriteriaDocument cd = d.criteria();
        SegmentCriteria c = cd == null ? null : new SegmentCriteria(cd.minTotalSpend(), cd.minOrderCount(), cd.country());
        return CustomerSegment.reconstitute(d.id(), d.code(), d.name(), d.description(),
                SegmentType.valueOf(d.type()), c, SegmentStatus.valueOf(d.status()), d.createdAt(), d.updatedAt());
    }

    private static String lower(String s) { return s.trim().toLowerCase(Locale.ROOT); }
}
