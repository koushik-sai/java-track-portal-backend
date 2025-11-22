package com.javaportal.dto;

import java.time.LocalDateTime;

import com.javaportal.entity.EventType;
import com.javaportal.entity.Status;

import lombok.Data;

@Data
public class NonTechnicalEventDTO {
	public Integer getNonTechnicalEventId() {
		return nonTechnicalEventId;
	}

	public void setNonTechnicalEventId(Integer nonTechnicalEventId) {
		this.nonTechnicalEventId = nonTechnicalEventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Integer getEventCoordinator() {
		return eventCoordinator;
	}

	public void setEventCoordinator(Integer eventCoordinator) {
		this.eventCoordinator = eventCoordinator;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	private Integer nonTechnicalEventId;
	private String eventName;
	private String location;
	private String description;
	
	private EventType type = EventType.NON_TECHNICAL;
	private Integer eventCoordinator;
	
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	private Status status;
}
