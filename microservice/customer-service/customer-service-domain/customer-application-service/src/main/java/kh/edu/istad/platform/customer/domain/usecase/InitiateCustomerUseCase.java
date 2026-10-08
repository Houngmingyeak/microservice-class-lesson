package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitiateCustomerUseCase {

    public void execute(InitiateCustomerCommand command){
        log.info("initiate ");
    }

}
