// InitiatePaymentUseCaseImpl.java
package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.platform.payment.domain.dto.InitiatePaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.event.PaymentCompletedEvent;
import kh.edu.istad.platform.payment.domain.event.PaymentEvent;
import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.domain.port.out.PaymentRepository;
import kh.edu.istad.platform.payment.domain.service.PaymentDomainService;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.domain.valueobject.OrderId;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class InitiatePaymentUseCaseImpl implements InitiatePaymentUseCase {

    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    public InitiatePaymentUseCaseImpl(PaymentDomainService paymentDomainService,
                                      PaymentRepository paymentRepository,
                                      CreditEntryRepository creditEntryRepository,
                                      CreditHistoryRepository creditHistoryRepository) {
        this.paymentDomainService = paymentDomainService;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
    }

    @Override
    @Transactional
    public PaymentResult execute(InitiatePaymentCommand command) {
        OrderId orderId = new OrderId(command.orderId());
        CustomerId customerId = new CustomerId(command.customerId());

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new PaymentDomainException("Payment for order " + orderId.value() + " already exists");
        }

        Payment payment = Payment.Builder.builder()
                .orderId(orderId)
                .customerId(customerId)
                .price(new Money(command.price()))
                .build();

        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new PaymentDomainException(
                        "Customer " + customerId.value() + " has no credit entry"));

        List<CreditHistory> creditHistories =
                new ArrayList<>(creditHistoryRepository.findByCustomerId(customerId));
        int oldSize = creditHistories.size();
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent event = paymentDomainService.validateAndInitiatePayment(
                payment, creditEntry, creditHistories, failureMessages);

        // the credit is only changed when the payment really completed
        if (event instanceof PaymentCompletedEvent) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.saveAll(creditHistories.subList(oldSize, creditHistories.size()));
        }
        paymentRepository.save(payment);

        return PaymentResult.from(payment, failureMessages);
    }
}