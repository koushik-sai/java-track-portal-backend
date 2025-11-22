package com.javaportal.service;

import java.util.List;

import com.javaportal.dto.NotificationDTO;
import com.javaportal.exception.JavaPortalException;

public interface NotificationService {
	public List<NotificationDTO> getPastTwoDaysNotifications() throws JavaPortalException;
	public boolean readAllNotifications(Integer[] notificationIds) throws JavaPortalException;
	public boolean sendNotification(NotificationDTO dto) throws JavaPortalException;
	public boolean markNotificationAsRead(Integer notificationId) throws JavaPortalException;
}
