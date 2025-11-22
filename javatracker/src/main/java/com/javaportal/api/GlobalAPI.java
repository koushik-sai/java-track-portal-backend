package com.javaportal.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.dto.NonTechnicalEventDTO;
import com.javaportal.dto.TrainingEventDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.AdminService;
import com.javaportal.service.EventCoordinatorService;
import com.javaportal.service.OperationsAnchorService;

@CrossOrigin
@RestController
@RequestMapping(value = "api/global")
public class GlobalAPI {
	
	private AdminService adminService;
	private EventCoordinatorService coordinatorService;
	private OperationsAnchorService anchorService;
	
	public GlobalAPI(AdminService adminService, EventCoordinatorService coordinatorService, OperationsAnchorService anchorService) {
		this.adminService = adminService;
		this.coordinatorService = coordinatorService;
		this.anchorService = anchorService;
	}
	
	@GetMapping("/getTeamNames")
	public ResponseEntity<List<String>> getTeamNames() throws JavaPortalException {
		List<String> listOfTeamNames = adminService.getAllTeamNames();
		return new ResponseEntity<>(listOfTeamNames, HttpStatus.OK);
	}
	
	@GetMapping("/getAllFromTeam/{teamName}")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployeesFromTeam(@PathVariable String teamName) throws JavaPortalException {
		List<EmployeeDTO> listOfEmployees = adminService.getEmployeesByTeam(teamName);
		return new ResponseEntity<>(listOfEmployees, HttpStatus.OK);
	}
	
	@GetMapping("getAllTrainingEvents")
	public ResponseEntity<List<TrainingEventDTO>> getAllTrainingEvents() throws JavaPortalException {
		List<TrainingEventDTO> listOfEvents = anchorService.getAllEvents();
		return new ResponseEntity<>(listOfEvents, HttpStatus.OK);
	}
	
	@GetMapping("getAllNonTechnicalEvents")
	public ResponseEntity<List<NonTechnicalEventDTO>> getAllNonTechnicalEvents() throws JavaPortalException {
		List<NonTechnicalEventDTO> listOfEvents = coordinatorService.getAllEvents();
		return new ResponseEntity<>(listOfEvents, HttpStatus.OK);
	}
}

