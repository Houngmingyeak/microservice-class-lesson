package kh.edu.istad.platform.customer.bean;

import kh.edu.istad.platform.customer.domain.port.out.CustomerExistencePort;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import kh.edu.istad.platform.customer.domain.port.out.SegmentMembershipRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainServiceImpl;
import kh.edu.istad.platform.customer.domain.service.CustomerSegmentDomainService;
import kh.edu.istad.platform.customer.domain.service.CustomerSegmentDomainServiceImpl;
import kh.edu.istad.platform.customer.domain.usecase.CustomerSegmentUseCase;
import kh.edu.istad.platform.customer.domain.usecase.CustomerSegmentUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }

    @Bean
    public CustomerSegmentDomainService customerSegmentDomainService() {
        return new CustomerSegmentDomainServiceImpl();
    }

    @Bean
    public CustomerSegmentUseCase customerSegmentUseCase(
            CustomerSegmentRepository segments,
            SegmentMembershipRepository memberships,
            CustomerExistencePort customers) {
        return new CustomerSegmentUseCaseImpl(segments, memberships, customers);
    }
}