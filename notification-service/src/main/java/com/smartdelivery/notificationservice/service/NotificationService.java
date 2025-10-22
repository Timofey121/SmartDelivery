package com.smartdelivery.notificationservice.service;

import com.smartdelivery.notificationservice.dto.NotificationRequest;
import com.smartdelivery.notificationservice.entity.NotificationHistory;
import com.smartdelivery.notificationservice.mapper.NotificationMapper;
import com.smartdelivery.notificationservice.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationMapper notificationMapper;

    public void sendNotification(NotificationRequest notificationRequest) {
        // Notification sending logic (e.g., through RabbitMQ)
        NotificationHistory notification = notificationMapper.notificationRequestToNotificationHistory(notificationRequest);

        // Add to history
        notificationHistoryRepository.save(notification);
    }

    public List<NotificationHistory> getAllNotifications() {
        return notificationHistoryRepository.findAll();
    }
}

