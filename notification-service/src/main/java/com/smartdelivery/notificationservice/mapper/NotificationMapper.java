package com.smartdelivery.notificationservice.mapper;

import com.smartdelivery.notificationservice.dto.NotificationRequest;
import com.smartdelivery.notificationservice.entity.NotificationHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationHistory notificationRequestToNotificationHistory(NotificationRequest request);
}