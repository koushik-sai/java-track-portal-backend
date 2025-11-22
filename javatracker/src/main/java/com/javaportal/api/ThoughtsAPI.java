package com.javaportal.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.dto.ThoughtsDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.ThoughtsService;

@CrossOrigin
@RestController
@RequestMapping(value = "api/thoughts")
public class ThoughtsAPI {
	
	@Autowired
	private ThoughtsService thoughtService;

	@Autowired
	private Environment env;
	
	@GetMapping(value="getAllFromLastTwoDays")
	public ResponseEntity<List<ThoughtsDTO>> getAllFromPastTwoDays() throws JavaPortalException {
		List<ThoughtsDTO> listOfDTOs = thoughtService.getFromLastTwoDays();
		return new ResponseEntity<>(listOfDTOs, HttpStatus.OK);
	}
	
	@GetMapping(value="getMyThoughts/{empId}")
	public ResponseEntity<List<ThoughtsDTO>> getMyThoughts(@PathVariable Integer empId) throws JavaPortalException {
		List<ThoughtsDTO> listOfDTOs = thoughtService.getAllThoughts(empId);
		return new ResponseEntity<>(listOfDTOs, HttpStatus.OK);
	}
	
	@PostMapping(value="addThought")
	public ResponseEntity<String> getAllPastThoughts(@RequestBody ThoughtsDTO dto) throws JavaPortalException {
		String message = "";
		try {
			thoughtService.addThought(dto);
			message = env.getProperty("Api.THOUGHT_CREATED");
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value="deleteThought/{thoughtId}")
	public ResponseEntity<String> deleteThought(@PathVariable Integer thoughtId) throws JavaPortalException {
		String message = "";
		try {
			thoughtService.deleteThought(thoughtId);
			message = env.getProperty("Api.THOUGHT_DELETE_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
