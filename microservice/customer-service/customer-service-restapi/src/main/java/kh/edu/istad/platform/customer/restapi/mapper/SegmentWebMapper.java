package kh.edu.istad.platform.customer.restapi.mapper;

import kh.edu.istad.platform.customer.domain.dto.SegmentCommands.CreateSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.SegmentCommands.UpdateSegmentCommand;
import kh.edu.istad.platform.customer.domain.dto.SegmentResult;
import kh.edu.istad.platform.customer.domain.valueobject.SegmentCriteria;
import kh.edu.istad.platform.customer.restapi.dto.SegmentDtos.*;
import org.springframework.stereotype.Component;

@Component
public class SegmentWebMapper {

    public CreateSegmentCommand toCommand(CreateSegmentRequest r) {
        return new CreateSegmentCommand(r.code(), r.name(), r.description(), r.type(), toCriteria(r.criteria()));
    }

    public UpdateSegmentCommand toCommand(String id, UpdateSegmentRequest r) {
        return new UpdateSegmentCommand(id, r.name(), r.description(), toCriteria(r.criteria()));
    }

    public SegmentResponse toResponse(SegmentResult s) {
        SegmentCriteria c = s.criteria();
        CriteriaDto cd = c == null ? null : new CriteriaDto(c.minTotalSpend(), c.minOrderCount(), c.country());
        return new SegmentResponse(s.id(), s.code(), s.name(), s.description(), s.type(), cd, s.status(),
                s.memberCount(), s.createdAt(), s.updatedAt());
    }

    private SegmentCriteria toCriteria(CriteriaDto d) {
        return d == null ? null : new SegmentCriteria(d.minTotalSpend(), d.minOrderCount(), d.country());
    }
}
