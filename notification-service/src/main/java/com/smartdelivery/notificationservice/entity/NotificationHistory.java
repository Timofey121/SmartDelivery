package com.smartdelivery.notificationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String recipient;
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
    
    private LocalDateTime timestamp;
}
