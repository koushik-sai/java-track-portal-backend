package com.javaportal.entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empId;

    private String name;
    private String emailId;
    private String password;
    private Long contact;
    private String address;
    
    @ElementCollection
    @CollectionTable(
        name = "employee_area_of_interest",
        joinColumns = @JoinColumn(name = "employee_emp_id")
    )
    @Column(name = "interest")
    private List<String> areaOfInterest;
    
    @ElementCollection
    @CollectionTable(
        name = "employee_expertise",
        joinColumns = @JoinColumn(name = "employee_emp_id")
    )
    @Column(name = "expertise")
    private List<String> expertise;
    
    private String cabinDetails;
    private String responsibility;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @ManyToOne
    @JoinColumn(name="manager_id")
    private Employee manager;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    
    private boolean firstLogin = true;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}
}
