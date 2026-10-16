package kh.edu.istad.platform.business.restapi.mapper;

import kh.edu.istad.platform.business.domain.dto.ProductCommand;
import kh.edu.istad.platform.business.domain.dto.ProductResult;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessResult;
import kh.edu.istad.platform.business.restapi.dto.ProductRequest;
import kh.edu.istad.platform.business.restapi.dto.ProductResponse;
import kh.edu.istad.platform.business.restapi.dto.RegisterBusinessRequest;
import kh.edu.istad.platform.business.restapi.dto.RegisterBusinessResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessWebMapper {

    RegisterBusinessCommand toCommand(RegisterBusinessRequest request);

    ProductCommand toProductCommand(ProductRequest request);

    RegisterBusinessResponse toResponse(RegisterBusinessResult result);

    ProductResponse toProductResponse(ProductResult result);

}
