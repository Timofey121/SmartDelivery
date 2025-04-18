package com.timofey.orderservice.mapper;

import com.timofey.orderservice.dto.OrderRequest;
import com.timofey.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order OrderRequestToOrder(OrderRequest request);
}