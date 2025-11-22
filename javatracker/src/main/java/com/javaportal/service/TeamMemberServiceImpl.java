package com.javaportal.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.entity.Employee;
import com.javaportal.entity.Notification;
import com.javaportal.entity.QueryStatus;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;
import com.javaportal.repository.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TeamMemberServiceImpl implements TeamMemberService {

	@Autowired
	private EmployeeRepository empRepo;
	
	@Autowired
	private NotificationRepository notifRepo;
	
	@Autowired
	private Environment env;
	
	@Override
	public EmployeeDTO getEmployeeById(Integer empId) throws JavaPortalException {
		Optional<Employee> optEmp = empRepo.findById(empId);
		Employee emp = optEmp.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));
		
		EmployeeDTO dto = new EmployeeDTO();
		dto.setEmpId(emp.getEmpId());
		dto.setEmailId(emp.getEmailId());
		dto.setName(emp.getName());
		dto.setAddress(emp.getAddress());
		dto.setAreaOfInterest(emp.getAreaOfInterest());
		dto.setCabinDetails(emp.getCabinDetails());
		dto.setContact(emp.getContact());
		dto.setExpertise(emp.getExpertise());
		dto.setFirstLogin(emp.isFirstLogin());
		dto.setManagerId(emp.getManager()==null?null:emp.getManager().getEmpId());
		dto.setResponsibility(emp.getResponsibility());
		dto.setRole(emp.getRole());
		dto.setTeamId(emp.getTeam()==null?null:emp.getTeam().getTeamId());
		
		return dto;
	}

	@Override
	public boolean updateEmployeeDetails(EmployeeDTO dto) throws JavaPortalException {
		Optional<Employee> optEmp = empRepo.findById(dto.getEmpId());
		Employee emp = optEmp.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));
		
		emp.setAreaOfInterest(dto.getAreaOfInterest());
		emp.setExpertise(dto.getExpertise());
		
		Notification notif = new Notification();
		notif.setEmployee(emp);
		notif.setManager(emp.getManager());
		notif.setTimeStamp(LocalDateTime.now());
		notif.setMessage("Your team member " + emp.getName() + " with employee ID: " + emp.getEmpId() + " " + env.getProperty("Service.NOTIFICATION_MESSAGE"));
		notif.setStatus(QueryStatus.UNREAD);
		
		notifRepo.save(notif);
		
		empRepo.save(emp);
		return true;
	}

}
