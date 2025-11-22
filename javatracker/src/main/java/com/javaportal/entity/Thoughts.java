package com.javaportal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Thoughts {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int thoughtId;
	
	@ManyToOne
	@JoinColumn(name="emp_id")
	private Employee employee;
	
	private String empName;
	
	@Column(length=250)
	private String message;
	
	private LocalDateTime sharedOn;

	public int getThoughtId() {
		return thoughtId;
	}

	public void setThoughtId(int thoughtId) {
		this.thoughtId = thoughtId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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
}
