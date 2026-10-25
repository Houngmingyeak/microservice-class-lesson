package kh.edu.istad.platform.payment.bean;

import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.domain.port.out.PaymentRepository;
import kh.edu.istad.platform.payment.domain.service.PaymentDomainService;
import kh.edu.istad.platform.payment.domain.service.PaymentDomainServiceImpl;
import kh.edu.istad.platform.payment.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }

    @Bean
    public InitiatePaymentUseCase initiatePaymentUseCase(PaymentDomainService paymentDomainService,
                                                         PaymentRepository paymentRepository,
                                                         CreditEntryRepository creditEntryRepository,
                                                         CreditHistoryRepository creditHistoryRepository) {
        return new InitiatePaymentUseCaseImpl(paymentDomainService, paymentRepository,
                creditEntryRepository, creditHistoryRepository);
    }

    @Bean
    public CancelPaymentUseCase cancelPaymentUseCase(PaymentDomainService paymentDomainService,
                                                     PaymentRepository paymentRepository,
                                                     CreditEntryRepository creditEntryRepository,
                                                     CreditHistoryRepository creditHistoryRepository) {
        return new CancelPaymentUseCaseImpl(paymentDomainService, paymentRepository,
                creditEntryRepository, creditHistoryRepository);
    }

    @Bean
    public AddCreditUseCase addCreditUseCase(CreditEntryRepository creditEntryRepository,
                                             CreditHistoryRepository creditHistoryRepository) {
        return new AddCreditUseCaseImpl(creditEntryRepository, creditHistoryRepository);
    }

    @Bean
    public GetPaymentUseCase getPaymentUseCase(PaymentRepository paymentRepository) {
        return new GetPaymentUseCaseImpl(paymentRepository);
    }
}