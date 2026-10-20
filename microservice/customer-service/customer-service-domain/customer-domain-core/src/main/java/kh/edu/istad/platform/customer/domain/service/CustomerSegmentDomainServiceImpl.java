package kh.edu.istad.platform.customer.domain.service;

import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentStatus;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentType;

import java.math.BigDecimal;

public class CustomerSegmentDomainServiceImpl implements CustomerSegmentDomainService {
    @Override
    public boolean isEligible(CustomerSegment s, BigDecimal totalSpend, int orderCount, String country) {
        return s.getStatus() == SegmentStatus.ACTIVE
                && s.getType() == SegmentType.DYNAMIC
                && s.getCriteria() != null
                && s.getCriteria().matches(totalSpend, orderCount, country);
    }
}
