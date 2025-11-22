package com.javaportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.entity.Employee;
import com.javaportal.entity.Role;
import com.javaportal.entity.Team;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;
import com.javaportal.repository.TeamRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {
	
	@Autowired
	private EmployeeRepository empRepo;
	
	
	@Autowired
	private TeamRepository teamRepo;

	@Override
	public boolean addMemberToTeam(Integer managerId, Integer empId) throws JavaPortalException {
		
		Optional<Employee> optEmp=empRepo.findById(empId);
		Employee emp=optEmp.orElseThrow(()->new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));
		
		if(emp.getManager()!=null || emp.getTeam()!=null) throw new JavaPortalException("Service.EMPLOYEE_ALREADY_ASSIGNED");
		
		Optional<Employee> optManager=empRepo.findById(managerId);
		Employee manager=optManager.get();
		
		emp.setManager(manager);
		emp.setTeam(manager.getTeam());
		
		Optional<Team> optTeam=teamRepo.findById(manager.getTeam().getTeamId());
		Team team=optTeam.get();
		
		if(team.getTeamSize()>=15) throw new JavaPortalException("Service.TEAM_FULL");
		
		team.setTeamSize(team.getTeamSize()+1);
		
		empRepo.save(emp);
		teamRepo.save(team);

		return true;
	}
	
	@Override
	public boolean updateMember(EmployeeDTO empDTO) throws JavaPortalException {
		
		Optional<Employee> optEmp=empRepo.findById(empDTO.getEmpId());
		Employee emp=optEmp.orElseThrow(()->new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));
		
		emp.setCabinDetails(empDTO.getCabinDetails());
		emp.setResponsibility(empDTO.getResponsibility());
		emp.setRole(empDTO.getRole());
		
		empRepo.save(emp);
		
		return true;
	}

	@Override
	public boolean removeFromTeam(Integer empId) throws JavaPortalException {
		
		Optional<Employee> emp=empRepo.findById(empId);
		if(emp.isEmpty()) throw new JavaPortalException("Service.EMPLOYEE_NOT_FOUND");
		
		Employee employee=emp.get();
		
		Optional<Team> optTeam=teamRepo.findById(employee.getTeam().getTeamId());
		Team team=optTeam.get();
		
		
		
		team.setTeamSize(team.getTeamSize()-1);
		
		teamRepo.save(team);
		
		employee.setTeam(null);
		employee.setManager(null);
		
		empRepo.save(employee);
		
		return true;
	}

	@Override
	public EmployeeDTO getEmployeeDetails(Integer empId) throws JavaPortalException {
		Optional<Employee> optEmp = empRepo.findById(empId);
		Employee e = optEmp.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));
		
		EmployeeDTO edto = new EmployeeDTO();
		edto.setEmpId(e.getEmpId());
		edto.setName(e.getName());
		edto.setEmailId(e.getEmailId());
		edto.setContact(e.getContact());
		edto.setAddress(e.getAddress());
		edto.setAreaOfInterest(e.getAreaOfInterest());
		edto.setExpertise(e.getExpertise());
		edto.setCabinDetails(e.getCabinDetails());
		edto.setResponsibility(e.getResponsibility());
		edto.setRole(e.getRole());
		edto.setManagerId(e.getManager()==null?1:e.getManager().getEmpId());
		edto.setTeamId(e.getTeam().getTeamId());
		edto.setFirstLogin(e.isFirstLogin());
		
		return edto;
	}

	@Override
	public List<EmployeeDTO> getEmployeeByManagerId(Integer managerId) throws JavaPortalException {
		List<Employee> listOfEmployees =  empRepo.findByManager_EmpId(managerId);
		
		List<EmployeeDTO> listOfDtos = new ArrayList<>();
		for (Employee e : listOfEmployees) {
			EmployeeDTO edto = new EmployeeDTO();
			edto.setEmpId(e.getEmpId());
			edto.setName(e.getName());
			edto.setEmailId(e.getEmailId());
			edto.setContact(e.getContact());
			edto.setAddress(e.getAddress());
			edto.setAreaOfInterest(e.getAreaOfInterest());
			edto.setExpertise(e.getExpertise());
			edto.setCabinDetails(e.getCabinDetails());
			edto.setResponsibility(e.getResponsibility());
			edto.setRole(e.getRole());
			edto.setManagerId(e.getManager()==null?1:e.getManager().getEmpId());
			edto.setTeamId(e.getTeam()==null?null:e.getTeam().getTeamId());
			edto.setFirstLogin(e.isFirstLogin());
			
			listOfDtos.add(edto);
		}
		
		return listOfDtos;
	}

	@Override
	public List<EmployeeDTO> getEmployeeNotInAnyTeam() throws JavaPortalException {
		List<Employee> listOfEmployees = empRepo.findByManagerIsNullAndTeamIsNull();
		
		List<EmployeeDTO> listOfDtos = new ArrayList<>();
		
		for(Employee e : listOfEmployees) {
			if (e.getRole() == Role.ADMIN) continue;
			if(e.getRole() == Role.MANAGER) continue;
			EmployeeDTO edto = new EmployeeDTO();
			edto.setEmpId(e.getEmpId());
			edto.setName(e.getName());
			edto.setEmailId(e.getEmailId());
			edto.setContact(e.getContact());
			edto.setAddress(e.getAddress());
			edto.setAreaOfInterest(e.getAreaOfInterest());
			edto.setExpertise(e.getExpertise());
			edto.setCabinDetails(e.getCabinDetails());
			edto.setResponsibility(e.getResponsibility());
			edto.setRole(e.getRole());
			edto.setManagerId(null);
			edto.setTeamId(null);
			edto.setFirstLogin(e.isFirstLogin());
			
			listOfDtos.add(edto);
		}
		return listOfDtos;
	}

	

}

