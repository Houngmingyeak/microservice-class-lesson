package kh.edu.istad.platform.business;

import kh.edu.istad.common.domain.valueobject.Money;

import java.math.BigDecimal;

public class BusinessApplicationService {
    static void main(String[] args) {
        Money money = new Money(BigDecimal.valueOf(100));
        money.isGreaterThanZero();
        System.out.println(money.amount());
    }

}
