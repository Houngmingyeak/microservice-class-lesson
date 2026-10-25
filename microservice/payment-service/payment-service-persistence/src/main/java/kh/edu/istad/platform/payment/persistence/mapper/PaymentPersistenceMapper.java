// PaymentPersistenceMapper.java
package kh.edu.istad.platform.payment.persistence.mapper;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.valueobject.*;
import kh.edu.istad.platform.payment.persistence.entity.CreditEntryEntity;
import kh.edu.istad.platform.payment.persistence.entity.CreditHistoryEntity;
import kh.edu.istad.platform.payment.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

    // ---------- Payment ----------
    public PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.setPaymentId(payment.getId().value());
        entity.setOrderId(payment.getOrderId().value());
        entity.setCustomerId(payment.getCustomerId().value());
        entity.setPrice(payment.getPrice().amount());
        entity.setPaymentStatus(payment.getPaymentStatus());
        entity.setCreatedAt(payment.getCreatedAt());
        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        return Payment.Builder.builder()
                .paymentId(new PaymentId(entity.getPaymentId()))
                .orderId(new OrderId(entity.getOrderId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .price(new Money(entity.getPrice()))
                .paymentStatus(entity.getPaymentStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    // ---------- CreditEntry ----------
    public CreditEntryEntity toEntity(CreditEntry creditEntry) {
        CreditEntryEntity entity = new CreditEntryEntity();
        entity.setCreditEntryId(creditEntry.getId().value());
        entity.setCustomerId(creditEntry.getCustomerId().value());
        entity.setTotalCreditAmount(creditEntry.getTotalCreditAmount().amount());
        return entity;
    }

    public CreditEntry toDomain(CreditEntryEntity entity) {
        return new CreditEntry(
                new CreditEntryId(entity.getCreditEntryId()),
                new CustomerId(entity.getCustomerId()),
                new Money(entity.getTotalCreditAmount()));
    }

    // ---------- CreditHistory ----------
    public CreditHistoryEntity toEntity(CreditHistory history) {
        CreditHistoryEntity entity = new CreditHistoryEntity();
        entity.setCreditHistoryId(history.getId().value());
        entity.setCustomerId(history.getCustomerId().value());
        entity.setAmount(history.getAmount().amount());
        entity.setTransactionType(history.getTransactionType());
        return entity;
    }

    public CreditHistory toDomain(CreditHistoryEntity entity) {
        return new CreditHistory(
                new CreditHistoryId(entity.getCreditHistoryId()),
                new CustomerId(entity.getCustomerId()),
                new Money(entity.getAmount()),
                entity.getTransactionType());
    }
}