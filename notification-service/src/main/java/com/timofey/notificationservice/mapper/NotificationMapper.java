package com.timofey.notificationservice.mapper;

import com.timofey.notificationservice.dto.NotificationRequest;
import com.timofey.notificationservice.entity.NotificationHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationHistory NotificationRequestToNotificationHistory(NotificationRequest request);
}