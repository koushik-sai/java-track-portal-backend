package com.javaportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.dto.TeamDTO;
import com.javaportal.entity.Employee;
import com.javaportal.entity.Role;
import com.javaportal.entity.Team;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;
import com.javaportal.repository.TeamRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {
	
	@Autowired private TeamRepository teamRepo;
	
	@Autowired private EmployeeRepository empRepo;

	@Override
	public List<TeamDTO> getAllTeams() throws JavaPortalException {
		List<Team> listOfTeams = teamRepo.findAll();
		
		List<TeamDTO> listOfDtos = new ArrayList<>();
		
		for (Team t : listOfTeams) {
			TeamDTO dto = new TeamDTO();
			dto.setTeamId(t.getTeamId());
			dto.setTeamName(t.getTeamName());
			dto.setDescription(t.getDescription());
			dto.setTeamSize(t.getTeamSize());
			dto.setManagerId(t.getManager()!=null?t.getManager().getEmpId():null);
			
			listOfDtos.add(dto);
		}
		
		return listOfDtos;
	}

	@Override
	public boolean deleteTeam(Integer teamId) throws JavaPortalException {
		Optional<Team> optTeam = teamRepo.findById(teamId);
		Team t = optTeam.orElseThrow(() -> new JavaPortalException("Service.TEAM_NOT_FOUND"));
		
		List<Employee> listOfEmployees = empRepo.findByTeam_TeamId(teamId);
		
		for (Employee emp : listOfEmployees) {
			emp.setTeam(null);
			emp.setManager(null);
		}
		
		teamRepo.delete(t);
		
		return false;
	}

	@Override
	public Integer createTeam(TeamDTO dto) throws JavaPortalException {
		Optional<Team> optTeam = teamRepo.findByTeamName(dto.getTeamName());
		if (optTeam.isPresent()) throw new JavaPortalException("Service.TEAM_ALREADY_EXISTS");
		
		Employee emp = empRepo.findById(dto.getManagerId()).get();
		
		Team t = new Team();
		t.setTeamName(dto.getTeamName());
		t.setDescription(dto.getDescription());
		t.setTeamSize(dto.getTeamSize());
		t.setManager(emp);
		t.setTeamSize(1);

		teamRepo.save(t);
		emp.setTeam(t);
		
		return t.getTeamId();
	}

	@Override
	public List<EmployeeDTO> getManagersWithoutTeams() throws JavaPortalException {
		List<Employee> listOfEmps = empRepo.findByRoleAndTeamIsNull(Role.MANAGER);
		List<EmployeeDTO> listOfDtos = new ArrayList<>();
		
		for (Employee e : listOfEmps) {
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
			edto.setManagerId(e.getManager()==null?null:e.getManager().getEmpId());
			edto.setTeamId(e.getTeam()==null?null:e.getTeam().getTeamId());
			edto.setFirstLogin(e.isFirstLogin());
			
			listOfDtos.add(edto);
		}
		
		return listOfDtos;
	}
	
	
}
