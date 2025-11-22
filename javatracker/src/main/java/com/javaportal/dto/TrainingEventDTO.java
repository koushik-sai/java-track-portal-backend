package com.javaportal.dto;

import java.time.LocalDateTime;

import com.javaportal.entity.EventType;
import com.javaportal.entity.Status;

import lombok.Data;

@Data
public class TrainingEventDTO {
	private Integer trainingEventId;

	private String eventName;
	
	private final EventType type = EventType.TRAINING;

	private Integer operationsAnchor;
	
	private String location;
	
	private String description;
	
	public Integer getTrainingEventId() {
		return trainingEventId;
	}

	public void setTrainingEventId(Integer trainingEventId) {
		this.trainingEventId = trainingEventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Integer getOperationsAnchor() {
		return operationsAnchor;
	}

	public void setOperationsAnchor(Integer operationsAnchor) {
		this.operationsAnchor = operationsAnchor;
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

	public EventType getType() {
		return type;
	}

	private LocalDateTime startTime;

	private LocalDateTime endTime;
	
	private Status status;
	
}
