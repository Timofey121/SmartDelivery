package com.timofey.notificationservice.repository;

import com.timofey.notificationservice.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
}
