package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.domain.port.out.CustomerStatisticsPort;
import reactor.core.publisher.Mono;

public class GetCustomerStatisticsUseCaseImpl implements GetCustomerStatisticsUseCase {

    private final CustomerStatisticsPort statisticsPort;

    public GetCustomerStatisticsUseCaseImpl(CustomerStatisticsPort statisticsPort) {
        this.statisticsPort = statisticsPort;
    }

    @Override
    public Mono<CustomerStatistics> execute() {
        return statisticsPort.getStatistics();
    }
}

