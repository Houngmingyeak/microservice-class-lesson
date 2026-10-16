package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.platform.business.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalStatus;
import kh.edu.istad.platform.business.domain.valueobject.OrderId;

public class OrderApproval extends BaseEntity<OrderApprovalId> {

    private final BusinessId businessId;
    private final OrderId orderId;
    private final OrderApprovalStatus approvalStatus;

    public OrderApproval(OrderApprovalId orderApprovalId, BusinessId businessId, OrderId orderId,
                         OrderApprovalStatus approvalStatus) {
        super.setId(orderApprovalId);
        this.businessId = businessId;
        this.orderId = orderId;
        this.approvalStatus = approvalStatus;
    }

    public BusinessId getBusinessId() {
        return businessId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }
}
