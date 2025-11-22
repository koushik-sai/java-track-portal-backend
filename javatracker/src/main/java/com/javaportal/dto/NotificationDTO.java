package com.javaportal.dto;

import java.time.LocalDateTime;

import com.javaportal.entity.QueryStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationDTO {
	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public Integer getManager() {
		return manager;
	}

	public void setManager(Integer manager) {
		this.manager = manager;
	}

	public Integer getEmployee() {
		return employee;
	}

	public void setEmployee(Integer employee) {
		this.employee = employee;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public QueryStatus getStatus() {
		return status;
	}

	public void setStatus(QueryStatus status) {
		this.status = status;
	}

	private Integer notificationId;
	@NotNull(message="{notification.manager.absent}")
	@Valid
	private Integer manager;
	
	@NotNull(message="{notification.employee.absent}")
	@Valid
	private Integer employee;
	
	@NotNull(message="{notification.timestamp.absent}")
	//Manage the date constraint in the service layer for validation - date should be current date, not past nor future
	private LocalDateTime timeStamp;
	
	private String message;
	
	private QueryStatus status;
}
