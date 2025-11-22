package com.javaportal.dto;

import java.util.List;

import com.javaportal.entity.Role;

import lombok.Data;

@Data
public class EmployeeDTO {
	private Integer empId;
	private String name;
	private String emailId;
	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getContact() {
		return contact;
	}

	public void setContact(Long contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getAreaOfInterest() {
		return areaOfInterest;
	}

	public void setAreaOfInterest(List<String> areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}

	public List<String> getExpertise() {
		return expertise;
	}

	public void setExpertise(List<String> expertise) {
		this.expertise = expertise;
	}

	public String getCabinDetails() {
		return cabinDetails;
	}

	public void setCabinDetails(String cabinDetails) {
		this.cabinDetails = cabinDetails;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	private Long contact;
	private String address;
	
	private List<String> areaOfInterest;
	
	private List<String> expertise;
	
	private String cabinDetails;
	
	private String responsibility;
	
	private Role role;
	
	private Integer managerId;
	
	private Integer teamId;
	
	private boolean firstLogin = true;
}
