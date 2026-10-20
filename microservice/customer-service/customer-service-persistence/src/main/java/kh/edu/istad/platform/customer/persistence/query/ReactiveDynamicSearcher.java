package kh.edu.istad.platform.customer.persistence.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReactiveDynamicSearcher {

    private final ReactiveMongoTemplate template;

    public <T> Mono<Page<T>> search(DynamicQuery dynamicQuery, Pageable pageable, Class<T> type) {
        Mono<List<T>> content = template
                .find(dynamicQuery.build(pageable), type)
                .collectList();

        Mono<Long> total = template.count(dynamicQuery.build(), type);

        return Mono.zip(content, total)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }
}