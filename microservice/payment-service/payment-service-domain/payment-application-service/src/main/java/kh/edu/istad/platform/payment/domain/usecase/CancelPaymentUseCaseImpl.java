// CancelPaymentUseCaseImpl.java
package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.platform.payment.domain.dto.CancelPaymentCommand;
import kh.edu.istad.platform.payment.domain.dto.PaymentResult;
import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.event.PaymentCancelledEvent;
import kh.edu.istad.platform.payment.domain.event.PaymentEvent;
import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.exception.PaymentNotFoundException;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.domain.port.out.PaymentRepository;
import kh.edu.istad.platform.payment.domain.service.PaymentDomainService;
import kh.edu.istad.platform.payment.domain.valueobject.OrderId;
import kh.edu.istad.platform.payment.domain.valueobject.PaymentStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class CancelPaymentUseCaseImpl implements CancelPaymentUseCase {

    private final PaymentDomainService paymentDomainService;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    public CancelPaymentUseCaseImpl(PaymentDomainService paymentDomainService,
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
    public PaymentResult execute(CancelPaymentCommand command) {
        OrderId orderId = new OrderId(command.orderId());

        Payment existing = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(
                        "Payment for order " + orderId.value() + " not found"));

        if (existing.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new PaymentDomainException("Only COMPLETED payments can be cancelled");
        }

        // the domain service requires a payment without id (validatePayment), so we work on a copy
        Payment payment = Payment.Builder.builder()
                .orderId(existing.getOrderId())
                .customerId(existing.getCustomerId())
                .price(existing.getPrice())
                .createdAt(existing.getCreatedAt())
                .build();

        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(existing.getCustomerId())
                .orElseThrow(() -> new PaymentDomainException(
                        "Customer " + existing.getCustomerId().value() + " has no credit entry"));

        List<CreditHistory> creditHistories =
                new ArrayList<>(creditHistoryRepository.findByCustomerId(existing.getCustomerId()));
        int oldSize = creditHistories.size();
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent event = paymentDomainService.validateAndCancelPayment(
                payment, creditEntry, creditHistories, failureMessages);

        payment.setId(existing.getId());

        // on failure nothing is saved, so the stored payment stays COMPLETED
        if (event instanceof PaymentCancelledEvent) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.saveAll(creditHistories.subList(oldSize, creditHistories.size()));
            paymentRepository.save(payment);
        }

        return PaymentResult.from(payment, failureMessages);
    }
}