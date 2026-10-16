package kh.edu.istad.platform.order.restapi.mapper;

import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.dto.OrderItemCommand;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderRequest;
import kh.edu.istad.platform.order.restapi.dto.CreateOrderResponse;
import kh.edu.istad.platform.order.restapi.dto.OrderItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderWebMapper {

    @Mapping(source = "address.street", target = "street")
    @Mapping(source = "address.postalCode", target = "postalCode")
    @Mapping(source = "address.city", target = "city")
    CreateOrderCommand toCommand(CreateOrderRequest request);

    OrderItemCommand toItemCommand(OrderItemRequest request);

    CreateOrderResponse toResponse(CreateOrderResult result);

}
