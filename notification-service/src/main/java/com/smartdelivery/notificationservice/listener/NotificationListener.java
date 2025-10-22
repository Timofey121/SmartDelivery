package com.smartdelivery.notificationservice.listener;

import com.smartdelivery.notificationservice.dto.NotificationRequest;
import com.smartdelivery.notificationservice.mapper.NotificationMapper;
import com.smartdelivery.notificationservice.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final NotificationMapper notificationMapper;

    @RabbitListener(queues = "notification.queue")
    public void handle(NotificationRequest notificationRequest) {
        notificationHistoryRepository.save(notificationMapper.notificationRequestToNotificationHistory(notificationRequest));
        log.info("Notification received for order: {}", notificationRequest);
    }
}

