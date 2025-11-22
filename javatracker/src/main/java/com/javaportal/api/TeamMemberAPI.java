package com.javaportal.api;

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
import com.javaportal.service.TeamMemberService;

@CrossOrigin
@RestController
@RequestMapping(value = "api/teamMember")
public class TeamMemberAPI {
	
	@Autowired
	private TeamMemberService teamMemService;
	
	@Autowired
	private Environment env;
	
	@GetMapping("getMemberById/{empId}")
	public ResponseEntity<EmployeeDTO> getByEmployeeId(@PathVariable Integer empId) throws JavaPortalException {
		EmployeeDTO dto = teamMemService.getEmployeeById(empId);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@PutMapping("updateTeamMember")
	public ResponseEntity<String> updateEmployee(@RequestBody EmployeeDTO dto) throws JavaPortalException {
		String message = "";
		try {
			teamMemService.updateEmployeeDetails(dto);
			message = env.getProperty("Api.TEAM_MEMBER_UPDATE_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

