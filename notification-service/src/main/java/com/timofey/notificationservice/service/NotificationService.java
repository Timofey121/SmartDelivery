package com.timofey.notificationservice.service;

import com.timofey.notificationservice.dto.NotificationRequest;
import com.timofey.notificationservice.entity.NotificationHistory;
import com.timofey.notificationservice.mapper.NotificationMapper;
import com.timofey.notificationservice.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationMapper notificationMapper;

    public void sendNotification(NotificationRequest notificationRequest) {
        // Логика отправки уведомления (например, через RabbitMQ)
        NotificationHistory notification = notificationMapper.NotificationRequestToNotificationHistory(notificationRequest);

        // Добавление в историю
        notificationHistoryRepository.save(notification);
    }

    public List<NotificationHistory> getAllNotifications() {
        return notificationHistoryRepository.findAll();
    }
}

