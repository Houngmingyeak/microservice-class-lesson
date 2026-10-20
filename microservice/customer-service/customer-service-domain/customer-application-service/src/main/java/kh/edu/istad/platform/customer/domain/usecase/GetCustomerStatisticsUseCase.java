package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import reactor.core.publisher.Mono;

public interface GetCustomerStatisticsUseCase {
    Mono<CustomerStatistics> execute();
}