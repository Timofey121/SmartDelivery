package com.timofey.notificationservice.listener;

import com.timofey.notificationservice.dto.NotificationRequest;
import com.timofey.notificationservice.mapper.NotificationMapper;
import com.timofey.notificationservice.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationMapper notificationMapper;

    @RabbitListener(queues = "notification.queue")
    public void handle(NotificationRequest notificationRequest) {
        notificationHistoryRepository.save(notificationMapper.NotificationRequestToNotificationHistory(notificationRequest));
        log.info("Notification received for order: {}", notificationRequest);
    }
}

