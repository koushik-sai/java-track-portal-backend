package com.javaportal.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.dto.TeamDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.AdminService;
import com.javaportal.service.TeamService;

@CrossOrigin
@RestController
@RequestMapping(value = "api/admin")
public class AdminAPI {

	private final AdminService adminService;
    private final TeamService teamService;
    private final Environment env;

    public AdminAPI(AdminService adminService, TeamService teamService, Environment env) {
        this.adminService = adminService;
        this.teamService = teamService;
        this.env = env;
    }

	@GetMapping("/getAllFromTeam/{teamName}")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployeesFromTeam(@PathVariable String teamName)
			throws JavaPortalException {
		List<EmployeeDTO> listOfEmployees = new ArrayList<>();
		try {
			listOfEmployees = adminService.getEmployeesByTeam(teamName);
			return new ResponseEntity<>(listOfEmployees, HttpStatus.OK);
		} catch (JavaPortalException e) {
			return new ResponseEntity<>(listOfEmployees, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getEmployee/{empId}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Integer empId) throws JavaPortalException {
		EmployeeDTO empDTO = adminService.getEmployee(empId);
		return new ResponseEntity<>(empDTO, HttpStatus.OK);
	}

	@GetMapping("/getTeamNames")
	public ResponseEntity<List<String>> getTeamNames() throws JavaPortalException {
		List<String> listOfTeamNames = adminService.getAllTeamNames();
		return new ResponseEntity<>(listOfTeamNames, HttpStatus.OK);
	}

	@PostMapping("/createEmployee")
	public ResponseEntity<String> createEmployee(@RequestBody EmployeeDTO empDTO) throws JavaPortalException{
		String message = "";
		try {
			Integer empId = adminService.createEmployee(empDTO);
			message = env.getProperty("Api.EMPLOYEE_CREATED_SUCCESS") + " " + empId;
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (JavaPortalException e) {
			message = e.getMessage();
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/updateEmployee")
	public ResponseEntity<String> updateEmployee(@RequestBody EmployeeDTO empDTO) throws JavaPortalException {
		String message = "";
		try {
			adminService.updateEmployee(empDTO);
			message = env.getProperty("Api.EMPLOYEE_UPDATE_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = e.getMessage();
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteEmployee/{empId}")
	public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Integer empId) throws JavaPortalException {
	    Map<String, String> response = new HashMap<>();
	    try {
	        adminService.deleteEmployee(empId);
	        String message = env.getProperty("Api.EMPLOYEE_DELETE_SUCCESS") + " " + empId;
	        response.put("message", message);
	        return ResponseEntity.ok(response);
	    } catch (JavaPortalException e) {
	        response.put("error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


	@GetMapping(value = "getAllTeams")
	public ResponseEntity<List<TeamDTO>> getAllTeams() throws JavaPortalException {
		List<TeamDTO> listOfDtos = teamService.getAllTeams();
		return new ResponseEntity<>(listOfDtos, HttpStatus.OK);
	}

	@PostMapping(value = "createTeam")
	public ResponseEntity<String> createTeam(@RequestBody TeamDTO dto) throws JavaPortalException {
		String message = "";
		try {
			Integer id = teamService.createTeam(dto);
			message = env.getProperty("Api.TEAM_CREATE_SUCCESS") + " " + id;
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (JavaPortalException e) {
			message = e.getMessage();
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "deleteTeam/{teamId}")
	public ResponseEntity<String> deleteTeam(@PathVariable Integer teamId) throws JavaPortalException {
		String message = "";
		try {
			teamService.deleteTeam(teamId);
			message = env.getProperty("Api.TEAM_DELETE_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = e.getMessage();
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "getManagers")
	public ResponseEntity<List<EmployeeDTO>> getManagersWithoutTeams() throws JavaPortalException {
		List<EmployeeDTO> listOfDtos = teamService.getManagersWithoutTeams();
		return new ResponseEntity<>(listOfDtos, HttpStatus.OK);
	}
}
