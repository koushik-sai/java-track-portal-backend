package com.javaportal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaportal.dto.NotificationDTO;
import com.javaportal.entity.Notification;
import com.javaportal.entity.QueryStatus;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notifRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<NotificationDTO> getPastTwoDaysNotifications() throws JavaPortalException {
		LocalDateTime sentOn = LocalDateTime.now().minusDays(2);
		List<Notification> listOfNotifs = notifRepo.findByTimeStampGreaterThanEqual(sentOn);

		List<NotificationDTO> listOfNotifDTOs = new ArrayList<>();

		for (Notification n : listOfNotifs) {
			NotificationDTO dto = new NotificationDTO();
			dto.setNotificationId(n.getNotificationId());
			dto.setManager(n.getManager().getEmpId());
			dto.setEmployee(n.getEmployee().getEmpId());
			dto.setMessage(n.getMessage());
			dto.setStatus(n.getStatus());
			dto.setTimeStamp(n.getTimeStamp());
			
			listOfNotifDTOs.add(dto);
		}
		
		listOfNotifDTOs.sort((a, b) -> b.getTimeStamp().compareTo(a.getTimeStamp()));

		return listOfNotifDTOs;
	}

	@Override
	public boolean readAllNotifications(Integer[] notificationIds) throws JavaPortalException {
	    if (notificationIds == null || notificationIds.length == 0) {
	        throw new JavaPortalException("Service.NO_NOTIFICATION_IDS_PASSED");
	    }

	    List<Notification> notifications = notifRepo.findAllById(Arrays.asList(notificationIds));

	    for (Notification n : notifications) {
	        n.setStatus(QueryStatus.READ);
	    }

	    notifRepo.saveAll(notifications);

	    return true;
	}


	@Override
	public boolean sendNotification(NotificationDTO dto) throws JavaPortalException {
		Notification n = modelMapper.map(dto, Notification.class);
		n.setTimeStamp(LocalDateTime.now());
		n.setStatus(QueryStatus.UNREAD);
		
		notifRepo.save(n);
		
		return true;
	}

	@Override
	public boolean markNotificationAsRead(Integer notificationId) throws JavaPortalException {
		Optional<Notification> optQuery = notifRepo.findById(notificationId);
		Notification n = optQuery.orElseThrow(() -> new JavaPortalException("Service.NOTIFICATION_NOT_FOUND"));
		
		n.setStatus(QueryStatus.READ);
		
		notifRepo.save(n);
		
		return true;
	}

}
