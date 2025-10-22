package com.smartdelivery.orderservice.mapper;

import com.smartdelivery.orderservice.dto.OrderRequest;
import com.smartdelivery.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order orderRequestToOrder(OrderRequest request);
}