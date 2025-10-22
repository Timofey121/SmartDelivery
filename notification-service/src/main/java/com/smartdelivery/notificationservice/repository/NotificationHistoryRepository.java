package com.smartdelivery.notificationservice.repository;

import com.smartdelivery.notificationservice.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
}
