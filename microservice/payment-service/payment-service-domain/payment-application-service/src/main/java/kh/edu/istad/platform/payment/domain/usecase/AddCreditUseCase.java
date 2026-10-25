// AddCreditUseCase.java
package kh.edu.istad.platform.payment.domain.usecase;

import kh.edu.istad.platform.payment.domain.dto.AddCreditCommand;
import kh.edu.istad.platform.payment.domain.dto.CreditResult;

public interface AddCreditUseCase {
    CreditResult execute(AddCreditCommand command);
}