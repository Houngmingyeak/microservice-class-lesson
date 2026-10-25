// AddCreditUseCaseImpl.java
package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.common.domain.Money;
import kh.edu.istad.platform.payment.domain.dto.AddCreditCommand;
import kh.edu.istad.platform.payment.domain.dto.CreditResult;
import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.domain.valueobject.CreditEntryId;
import kh.edu.istad.platform.payment.domain.valueobject.CreditHistoryId;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.domain.valueobject.TransactionType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class AddCreditUseCaseImpl implements AddCreditUseCase {

    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    public AddCreditUseCaseImpl(CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository) {
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
    }

    @Override
    @Transactional
    public CreditResult execute(AddCreditCommand command) {
        if (command.amount() == null || command.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentDomainException("Credit amount must be greater than zero");
        }

        CustomerId customerId = new CustomerId(command.customerId());
        Money amount = new Money(command.amount());

        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(customerId)
                .orElseGet(() -> new CreditEntry(
                        new CreditEntryId(UUID.randomUUID()),
                        customerId,
                        new Money(BigDecimal.ZERO)));

        creditEntry.addCreditAmount(amount);
        creditEntryRepository.save(creditEntry);

        creditHistoryRepository.saveAll(List.of(new CreditHistory(
                new CreditHistoryId(UUID.randomUUID()),
                customerId,
                amount,
                TransactionType.CREDIT)));

        return new CreditResult(customerId.value(), creditEntry.getTotalCreditAmount().amount());
    }
}