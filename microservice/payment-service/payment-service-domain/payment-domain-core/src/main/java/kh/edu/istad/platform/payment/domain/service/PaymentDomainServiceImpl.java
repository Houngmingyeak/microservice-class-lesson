package kh.edu.istad.platform.payment.domain.service;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.event.PaymentCancelledEvent;
import kh.edu.istad.platform.payment.domain.event.PaymentCompletedEvent;
import kh.edu.istad.platform.payment.domain.event.PaymentEvent;
import kh.edu.istad.platform.payment.domain.event.PaymentFailedEvent;
import kh.edu.istad.platform.payment.domain.valueobject.CreditHistoryId;
import kh.edu.istad.platform.payment.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.payment.domain.valueobject.TransactionType;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();

        // the payment itself is not valid, so nothing is changed
        if (!failureMessages.isEmpty()) {
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
        }

        // the customer does not have enough credit, so nothing is changed
        if (payment.getPrice().amount().compareTo(creditEntry.getTotalCreditAmount().amount()) > 0) {
            failureMessages.add("Customer with id=" + payment.getCustomerId().value()
                    + " doesn't have enough credit for payment!");
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
        }

        // take the money and write it into the credit history
        creditEntry.subtractCreditAmount(payment.getPrice());
        creditHistories.add(new CreditHistory(
                new CreditHistoryId(UUID.randomUUID()),
                payment.getCustomerId(),
                payment.getPrice(),
                TransactionType.DEBIT
        ));

        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
        }
        payment.updateStatus(PaymentStatus.FAILED);
        return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessages) {
        payment.validatePayment(failureMessages);

        if (!failureMessages.isEmpty()) {
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
        }

        // give the money back and write it into the credit history
        creditEntry.addCreditAmount(payment.getPrice());
        creditHistories.add(new CreditHistory(
                new CreditHistoryId(UUID.randomUUID()),
                payment.getCustomerId(),
                payment.getPrice(),
                TransactionType.CREDIT
        ));

        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
        }
        payment.updateStatus(PaymentStatus.FAILED);
        return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
    }

    // the credit history must always explain the current credit of the customer
    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories,
                                       List<String> failureMessages) {
        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;
        for (CreditHistory history : creditHistories) {
            Money amount = history.getAmount();
            if (history.getTransactionType() == TransactionType.CREDIT) {
                totalCredit = totalCredit.add(amount.amount());
            } else {
                totalDebit = totalDebit.add(amount.amount());
            }
        }

        if (totalDebit.compareTo(totalCredit) > 0) {
            failureMessages.add("Customer with id=" + creditEntry.getCustomerId().value()
                    + " doesn't have enough credit according to credit history!");
        }

        if (creditEntry.getTotalCreditAmount().amount().compareTo(totalCredit.subtract(totalDebit)) != 0) {
            failureMessages.add("Credit history total is not equal to current credit for customer id: "
                    + creditEntry.getCustomerId().value() + "!");
        }
    }

}
