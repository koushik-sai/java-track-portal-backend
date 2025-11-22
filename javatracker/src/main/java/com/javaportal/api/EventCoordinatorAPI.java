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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaportal.dto.NonTechnicalEventDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.EventCoordinatorService;

@RestController
@CrossOrigin
@RequestMapping(value="api/eventCoordinator")
public class EventCoordinatorAPI {
	
	@Autowired
	private EventCoordinatorService coordinatorService;
	
	@Autowired
	private Environment env;
	
	@GetMapping("getEvents/{empId}")
	public ResponseEntity<List<NonTechnicalEventDTO>> getEventsById(@PathVariable Integer empId) throws JavaPortalException {
		List<NonTechnicalEventDTO> listOfEvents = coordinatorService.getEventsById(empId);
		
		return new ResponseEntity<>(listOfEvents, HttpStatus.OK);
	}
	
	@GetMapping(value="getEvent/{eventName}")
	public ResponseEntity<NonTechnicalEventDTO> getEventByName(@PathVariable String eventName) throws JavaPortalException {
		NonTechnicalEventDTO dto = coordinatorService.getEventByName(eventName);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@PostMapping("createEvent")
	public ResponseEntity<String> createEvent(@RequestBody NonTechnicalEventDTO dto) throws JavaPortalException {
		String message = "";
		try {
			Integer eventId = coordinatorService.createEvent(dto);
			message = env.getProperty("Api.EVENT_CREATED_SUCCESS") + " " + eventId;
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("updateEvent")
	public ResponseEntity<String> updateEvent(@RequestBody NonTechnicalEventDTO dto) throws JavaPortalException {
		String message = "";
		try {
			coordinatorService.updateEvent(dto);
			message = env.getProperty("Api.EVENT_UPDATE_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("deleteEvents")
	public ResponseEntity<String> deleteEvents() throws JavaPortalException {
		String message = "";
		try {
		coordinatorService.deleteEvents();
		message = env.getProperty("Api.EVENTS_DELETE_SUCCESS");
		return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("cancelEvent/{eventName}")
	public ResponseEntity<String> cancelEvent(@PathVariable String eventName) throws JavaPortalException {
		String message = "";
		try {
			coordinatorService.cancelEvent(eventName);
			message = env.getProperty("Api.CANCEL_EVENT_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = env.getProperty(e.getMessage());
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

