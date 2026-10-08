package kh.edu.istad.common.domain;

import java.math.BigDecimal;

public record Money(
        BigDecimal amount

) {

    public void isGreaterThanZero(){
        if (!(amount.compareTo(BigDecimal.ZERO) > 0)){
            System.out.println("Amount is not greater than zero");
            throw new RuntimeException("Amount is not greater than zero");
        }
    }
    //more logic
}
