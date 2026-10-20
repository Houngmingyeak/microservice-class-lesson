package kh.edu.istad.platform.customer.domain.valueobject;

import java.math.BigDecimal;

/** Rules a DYNAMIC segment uses to decide membership. Null field = rule not applied. */
public record SegmentCriteria(BigDecimal minTotalSpend, Integer minOrderCount, String country) {

    public SegmentCriteria {
        if (minTotalSpend != null && minTotalSpend.signum() < 0)
            throw new IllegalArgumentException("minTotalSpend must not be negative");
        if (minOrderCount != null && minOrderCount < 0)
            throw new IllegalArgumentException("minOrderCount must not be negative");
        if (country != null) {
            country = country.trim().toUpperCase();
            if (country.isEmpty()) country = null;
        }
    }

    public boolean isEmpty() {
        return minTotalSpend == null && minOrderCount == null && country == null;
    }

    public boolean matches(BigDecimal totalSpend, int orderCount, String customerCountry) {
        if (minTotalSpend != null && (totalSpend == null || totalSpend.compareTo(minTotalSpend) < 0)) return false;
        if (minOrderCount != null && orderCount < minOrderCount) return false;
        return country == null || (customerCountry != null && country.equalsIgnoreCase(customerCountry.trim()));
    }
}
