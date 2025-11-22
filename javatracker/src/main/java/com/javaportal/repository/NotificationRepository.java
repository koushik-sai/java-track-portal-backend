package com.javaportal.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaportal.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{
	List<Notification> findByTimeStampGreaterThanEqual(LocalDateTime sentOn);
}




