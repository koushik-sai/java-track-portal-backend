package com.javaportal.api;

import java.util.ArrayList;
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

import com.javaportal.dto.TrainingEventDTO;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.service.OperationsAnchorService;

@RestController
@CrossOrigin
@RequestMapping(value = "api/operationsAnchor")
public class OperationsAnchorAPI {
	@Autowired
	private OperationsAnchorService anchorService;

	@Autowired
	private Environment env;

	@GetMapping("getEventByName/{eventName}")
	public ResponseEntity<TrainingEventDTO> getEventByName(@PathVariable String eventName) throws JavaPortalException {
		TrainingEventDTO dto = anchorService.getEventByName(eventName);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@GetMapping("getEvents/{empId}")
	public ResponseEntity<List<TrainingEventDTO>> getEventsById(@PathVariable Integer empId) throws JavaPortalException{
		List<TrainingEventDTO> listOfEvents = new ArrayList<>();
		try {
			listOfEvents = anchorService.getEventsById(empId);
			return new ResponseEntity<>(listOfEvents,HttpStatus.OK);
		}  catch(JavaPortalException e) {
			return new ResponseEntity<>(listOfEvents, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("createEvent")
	public ResponseEntity<String> createEvent(@RequestBody TrainingEventDTO dto) throws JavaPortalException {
		String message = "";
		try {
			Integer eventId = anchorService.createTrainingEvent(dto);
			message = env.getProperty("Api.EVENT_CREATED_SUCCESS") + " " + eventId;
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch(JavaPortalException e) {
			message = e.getMessage();
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("updateEvent")
	public ResponseEntity<String> updateEvent(@RequestBody TrainingEventDTO dto) throws JavaPortalException {
		String message = "";
		try {
			anchorService.updateTrainingEvent(dto);
			message = env.getProperty("Api.EVENT_UPDATE_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = e.getMessage();
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("cancelEvent/{eventName}")
	public ResponseEntity<String> cancelEvent(@PathVariable String eventName) throws JavaPortalException {
		String message = "";
		try {
			anchorService.cancelEvent(eventName);
			message = env.getProperty("Api.CANCEL_EVENT_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = e.getMessage();
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("deleteEvents")
	public ResponseEntity<String> deleteEvents() throws JavaPortalException {
		String message = "";
		try {
			anchorService.deleteEvent();
			message = env.getProperty("Api.EVENTS_DELETE_SUCCESS");
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (JavaPortalException e) {
			message = e.getMessage();
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

