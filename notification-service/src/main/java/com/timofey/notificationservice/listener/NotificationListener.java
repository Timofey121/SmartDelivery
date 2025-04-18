package com.timofey.notificationservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @RabbitListener(queues = "notification.queue")
    public void handle(Order order) {
        log.info("Notification received for order: {}", order);
    }
}

