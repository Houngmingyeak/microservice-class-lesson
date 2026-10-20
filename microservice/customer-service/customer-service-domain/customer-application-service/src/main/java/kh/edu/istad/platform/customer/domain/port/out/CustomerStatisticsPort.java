package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import reactor.core.publisher.Mono;

public interface CustomerStatisticsPort {
    Mono<CustomerStatistics> getStatistics();
}