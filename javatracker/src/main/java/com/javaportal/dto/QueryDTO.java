package com.javaportal.dto;

import java.time.LocalDateTime;

import com.javaportal.entity.QueryStatus;

import lombok.Data;

@Data
public class QueryDTO {
	private Integer queryId;
	private String emailId;
	private String message;
	private LocalDateTime sentOn;
	private QueryStatus status;
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
	public LocalDateTime getSentOn() {
		return sentOn;
	}
	public void setSentOn(LocalDateTime sentOn) {
		this.sentOn = sentOn;
	}
	public QueryStatus getStatus() {
		return status;
	}
	public void setStatus(QueryStatus status) {
		this.status = status;
	}
}
