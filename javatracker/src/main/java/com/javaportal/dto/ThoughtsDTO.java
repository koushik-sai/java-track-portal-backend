package com.javaportal.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ThoughtsDTO {
	private int thoughtId;
	private Integer empId;
	private String empName;
	public int getThoughtId() {
		return thoughtId;
	}
	public void setThoughtId(int thoughtId) {
		this.thoughtId = thoughtId;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getSharedOn() {
		return sharedOn;
	}
	public void setSharedOn(LocalDateTime sharedOn) {
		this.sharedOn = sharedOn;
	}
	private String message;
	private LocalDateTime sharedOn;
}
