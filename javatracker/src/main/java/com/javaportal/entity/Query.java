package com.javaportal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Query {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer queryId;
	
	private String emailId;
	private String message;
	
	@Enumerated(EnumType.STRING)
	private QueryStatus status;
	
	private LocalDateTime sentOn;

	public Integer getQueryId() {
		return queryId;
	}

	public void setQueryId(Integer queryId) {
		this.queryId = queryId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public LocalDateTime getSentOn() {
		return sentOn;
	}

	public void setSentOn(LocalDateTime sentOn) {
		this.sentOn = sentOn;
	} 
}
