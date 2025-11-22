package com.javaportal.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.ManagerService;

@CrossOrigin
@RestController
@RequestMapping(value = "api/manager")
public class ManagerAPI {
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private Environment env;
	
	@GetMapping("getMemberDetails/{empId}")
	public ResponseEntity<EmployeeDTO> getMemberDetails(@PathVariable Integer empId) throws JavaPortalException {
		EmployeeDTO dto = managerService.getEmployeeDetails(empId);
		
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@GetMapping("getAllMembers/{managerId}")
	public ResponseEntity<List<EmployeeDTO>> getAllMembersOfTeam(@PathVariable Integer managerId) throws JavaPortalException {
		List<EmployeeDTO> listOfEmps = managerService.getEmployeeByManagerId(managerId);
		
		return new ResponseEntity<>(listOfEmps, HttpStatus.OK);
	}
	
	@GetMapping("getAllWithoutTeam")
	public ResponseEntity<List<EmployeeDTO>> getMembersWithoutTeam() throws JavaPortalException {
		List<EmployeeDTO> listOfEmps = managerService.getEmployeeNotInAnyTeam();
		
		return new ResponseEntity<>(listOfEmps, HttpStatus.OK);
	}
	
	@PutMapping(value = "addToTeam/{managerId}")
	public ResponseEntity<String> addToTeam(@PathVariable Integer managerId, @RequestBody Integer empId) throws JavaPortalException {
		String message = "";
		try {
			managerService.addMemberToTeam(managerId, empId);
			message = env.getProperty("Api.ADD_MEMBER_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("updateMember")
	public ResponseEntity<String> updateTeamMember(@RequestBody EmployeeDTO dto) throws JavaPortalException {
		String message = "";
		try {
			managerService.updateMember(dto);
			message = env.getProperty("Api.UPDATE_MEMBER_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("removeFromTeam")
	public ResponseEntity<String> removeFromTeam(@RequestBody Integer empId) throws JavaPortalException {
		String message = "";
		try {
			managerService.removeFromTeam(empId);
			message = env.getProperty("Api.REMOVE_MEMBER_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
