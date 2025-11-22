package com.javaportal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEvent {
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

	public Employee getOperationsAnchor() {
		return operationsAnchor;
	}

	public void setOperationsAnchor(Employee operationsAnchor) {
		this.operationsAnchor = operationsAnchor;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trainingEventId;
	
	private String eventName;
	
	@Enumerated(EnumType.STRING)
	private final EventType type = EventType.TRAINING;
	
	@ManyToOne
	@JoinColumn(name = "anchor_id")
	private Employee operationsAnchor;
	
	private String location;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	private Status status;

}

