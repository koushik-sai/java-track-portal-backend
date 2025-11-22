package com.javaportal.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.dto.NotificationDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.NotificationService;

@CrossOrigin
@RestController
@RequestMapping(value="api/notification")
public class NotificationAPI {
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private Environment env;
	
	@GetMapping(value = "/getNotifications")
	public ResponseEntity<List<NotificationDTO>> getPastTwoDaysQueries() throws JavaPortalException {
	    List<NotificationDTO> notificationList = notificationService.getPastTwoDaysNotifications();
	    return new ResponseEntity<>(notificationList, HttpStatus.OK);
	}
	
	// 2. POST send a new notification
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationDTO dto) throws JavaPortalException {
        boolean success = notificationService.sendNotification(dto);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(env.getProperty("Api.NOTIFICATION_SENT_SUCCESS"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(env.getProperty("Api.NOTIFICATION_SENT_ERROR"));
        }
    }

    // 3. PUT mark all notifications as read
    @PutMapping("/readAllNotifications")
    public ResponseEntity<String> readAllNotifications(@RequestBody Integer[] ids) throws JavaPortalException {
        boolean success = notificationService.readAllNotifications(ids);
        if (success) {
            return ResponseEntity.ok(env.getProperty("Api.NOTIFICATION_ALL_READ_SUCCESS"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(env.getProperty("Api.NOTIFICATION_ALL_READ_ERROR"));
        }
    }

    // 4. PUT mark one notification as read
    @PutMapping("/readNotification/{notificationId}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Integer notificationId) throws JavaPortalException {
        boolean success = notificationService.markNotificationAsRead(notificationId);
        if (success) {
            return ResponseEntity.ok("Api.NOTIFICATION_READ_SUCCESS");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(env.getProperty("Api.NOTIFICATION_NOT_FOUND"));
        }
    }
}

