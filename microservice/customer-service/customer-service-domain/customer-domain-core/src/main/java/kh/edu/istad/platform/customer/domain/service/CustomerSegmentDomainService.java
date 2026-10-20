package kh.edu.istad.platform.customer.domain.service;

import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;

import java.math.BigDecimal;

public interface CustomerSegmentDomainService {
    /** Does a customer with these metrics qualify for a DYNAMIC segment? */
    boolean isEligible(CustomerSegment segment, BigDecimal totalSpend, int orderCount, String country);
}
