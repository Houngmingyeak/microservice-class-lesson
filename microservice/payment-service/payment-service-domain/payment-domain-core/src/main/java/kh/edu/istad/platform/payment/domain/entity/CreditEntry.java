package kh.edu.istad.platform.payment.domain.entity;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.valueobject.CreditEntryId;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;

public class CreditEntry extends AggregateRoot<CreditEntryId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;

    public CreditEntry(CreditEntryId creditEntryId, CustomerId customerId, Money totalCreditAmount) {
        super.setId(creditEntryId);
        this.customerId = customerId;
        this.totalCreditAmount = totalCreditAmount;
    }

    public void addCreditAmount(Money amount) {
        if (amount == null || amount.amount() == null) {
            throw new PaymentDomainException("Amount to add must not be null");
        }
        totalCreditAmount = new Money(totalCreditAmount.amount().add(amount.amount()));
    }

    public void subtractCreditAmount(Money amount) {
        if (amount == null || amount.amount() == null) {
            throw new PaymentDomainException("Amount to subtract must not be null");
        }
        totalCreditAmount = new Money(totalCreditAmount.amount().subtract(amount.amount()));
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }
}
