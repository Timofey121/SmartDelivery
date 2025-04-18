package com.timofey.notificationservice.controller;

import com.timofey.notificationservice.dto.NotificationRequest;
import com.timofey.notificationservice.entity.NotificationHistory;
import com.timofey.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationService.sendNotification(notificationRequest);
        return new ResponseEntity<>("Notification sent to " + notificationRequest.getRecipient(), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<List<NotificationHistory>> getNotificationHistory() {
        List<NotificationHistory> history = notificationService.getAllNotifications();
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
