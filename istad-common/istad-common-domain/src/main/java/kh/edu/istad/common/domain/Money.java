package kh.edu.istad.common.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(
        BigDecimal amount

) {
    public boolean isGreaterThanZero(){
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isLessThanZero(){
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isGreaterThan(Money other){
        return this.amount != null && other.amount != null && this.amount.compareTo(other.amount) > 0;
    }

    public Money add(Money other){
        BigDecimal newAmount = this.amount.add(other.amount);
        return new Money(setScale(newAmount));
    }

    private BigDecimal setScale(BigDecimal inputAmount) {
        return inputAmount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money subtract(Money other){
        BigDecimal newAmount = this.amount.subtract(other.amount);
        return new Money(setScale(newAmount));
    }



//    public void isGreaterThanZero(){
//        if (!(amount.compareTo(BigDecimal.ZERO) > 0)){
//            System.out.println("Amount is not greater than zero");
//            throw new RuntimeException("Amount is not greater than zero");
//        }
//    }
    //more logic
}
