package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics.SegmentCount;
import kh.edu.istad.platform.customer.domain.port.out.CustomerStatisticsPort;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.document.CustomerSegmentDocument;
import kh.edu.istad.platform.customer.persistence.document.SegmentMembershipDocument;
import kh.edu.istad.platform.customer.persistence.repository.CustomerSegmentMongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomerStatisticsAdapter implements CustomerStatisticsPort {

    private final ReactiveMongoTemplate mongoTemplate;
    private final CustomerSegmentMongoRepository segmentRepository;

    @Override
    public Mono<CustomerStatistics> getStatistics() {
        return Mono.zip(countByStatus(), countBySegment())
                .map(t -> {
                    Map<String, Long> byStatus = t.getT1();
                    long total = byStatus.values().stream().mapToLong(Long::longValue).sum();
                    return new CustomerStatistics(total, byStatus, t.getT2());
                });
    }

    // GROUP BY status
    private Mono<Map<String, Long>> countByStatus() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group("status").count().as("total"));

        return mongoTemplate.aggregate(agg, CustomerDocument.class, Document.class)
                .collectMap(
                        d -> String.valueOf(d.get("_id")),
                        d -> ((Number) d.get("total")).longValue())
                .map(counts -> {
                    // ដាក់ 0 សម្រាប់ status ដែលគ្មាន customer
                    Map<String, Long> result = new LinkedHashMap<>();
                    for (CustomerStatus s : CustomerStatus.values()) {
                        result.put(s.name(), counts.getOrDefault(s.name(), 0L));
                    }
                    return result;
                });
    }

    // GROUP BY segmentId, បន្ទាប់មកភ្ជាប់ឈ្មោះ segment
    private Mono<List<SegmentCount>> countBySegment() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group("segmentId").count().as("total"),
                Aggregation.sort(Sort.Direction.DESC, "total"));

        return mongoTemplate.aggregate(agg, SegmentMembershipDocument.class, Document.class)
                .collectList()
                .flatMap(rows -> {
                    if (rows.isEmpty()) return Mono.just(List.<SegmentCount>of());

                    List<String> ids = rows.stream()
                            .map(r -> String.valueOf(r.get("_id")))
                            .toList();

                    return segmentRepository.findAllById(ids)
                            .collectMap(CustomerSegmentDocument::id, CustomerSegmentDocument::name)
                            .map(names -> rows.stream()
                                    .map(r -> {
                                        String segmentId = String.valueOf(r.get("_id"));
                                        return new SegmentCount(
                                                segmentId,
                                                names.getOrDefault(segmentId, "UNKNOWN"),
                                                ((Number) r.get("total")).longValue());
                                    })
                                    .toList());
                });
    }
}