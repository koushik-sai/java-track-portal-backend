package com.javaportal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaportal.dto.NonTechnicalEventDTO;
import com.javaportal.entity.Employee;
import com.javaportal.entity.NonTechnicalEvent;
import com.javaportal.entity.Status;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;
import com.javaportal.repository.NonTechnicalEventRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EventCoordinatorServiceImpl implements EventCoordinatorService {

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private NonTechnicalEventRepository nonTechEventRepo;

	@Autowired
	private ModelMapper modelMapper;
	

	@Override
	public Integer createEvent(NonTechnicalEventDTO nonTechEventDTO) throws JavaPortalException {
		// Check if start or end time is in the past
		LocalDateTime now = LocalDateTime.now();
		if (nonTechEventDTO.getStartTime().isBefore(now) || nonTechEventDTO.getEndTime().isBefore(now)) {
		    throw new JavaPortalException("Start time and end time must be in the future.");
		}
		

		// Check that the start time is before the end time
		if (nonTechEventDTO.getStartTime().isAfter(nonTechEventDTO.getEndTime())) {
			throw new JavaPortalException("Start time must be before end time.");
		}

		if (nonTechEventRepo.existsByEventName(nonTechEventDTO.getEventName())) {
			throw new JavaPortalException("Event name already exists.");
		}

		// Check for conflicting events at the same location
		List<NonTechnicalEvent> conflictingEvents = nonTechEventRepo.findConflictingEvents(
				nonTechEventDTO.getLocation(), nonTechEventDTO.getStartTime(), nonTechEventDTO.getEndTime());

		// If there are any conflicting events, throw an exception
		if (!conflictingEvents.isEmpty()) {
			throw new JavaPortalException("Service.EVENT_CONFLICT");
		}

		// Fetch the operations anchor (employee)
		Employee coordinator = empRepo.findById(nonTechEventDTO.getEventCoordinator())
				.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));

		// Map the DTO to the TrainingEvent entity
		NonTechnicalEvent event = modelMapper.map(nonTechEventDTO, NonTechnicalEvent.class);
		event.setEventCoordinator(coordinator);
		event.setStatus(Status.UPCOMING); // Handle the event status logic

		nonTechEventRepo.save(event);
		return event.getNonTechnicalEventId();
	}

	@Override
	public boolean updateEvent(NonTechnicalEventDTO eventDTO) throws JavaPortalException {
		LocalDateTime now = LocalDateTime.now();
	    if (eventDTO.getEndTime().isBefore(now)) {
	        throw new JavaPortalException("Cannot set event time in the past.");
	    }
		NonTechnicalEvent existingEvent = nonTechEventRepo.findById(eventDTO.getNonTechnicalEventId())
				.orElseThrow(() -> new JavaPortalException("Training event with the given ID does not exist!"));

		// Check if start time is before end time
		if (eventDTO.getStartTime().isAfter(eventDTO.getEndTime())) {
			throw new JavaPortalException("Start time must be before end time.");
		}

		// Check for duplicate event name (excluding the current event ID)
		boolean isDuplicateName = nonTechEventRepo.existsByEventNameAndNonTechnicalEventIdNot(eventDTO.getEventName(),
				eventDTO.getNonTechnicalEventId());
		if (isDuplicateName) {
			throw new JavaPortalException("Event name already exists.");
		}

		// Check for conflicting events (same location and overlapping time, excluding
		// current event)
		List<NonTechnicalEvent> conflictingEvents = nonTechEventRepo.findConflictingEventsExcludingCurrent(
				eventDTO.getLocation(), eventDTO.getStartTime(), eventDTO.getEndTime(),
				eventDTO.getNonTechnicalEventId());
		if (!conflictingEvents.isEmpty()) {
			throw new JavaPortalException("Service.EVENT_CONFLICT");
		}

		// Fetch the operations anchor (employee)
		Employee coordinator = empRepo.findById(eventDTO.getEventCoordinator())
				.orElseThrow(() -> new JavaPortalException("Operations anchor not found."));

		// Update fields
		existingEvent.setEventName(eventDTO.getEventName());
		existingEvent.setLocation(eventDTO.getLocation());
		existingEvent.setDescription(eventDTO.getDescription());
		existingEvent.setStartTime(eventDTO.getStartTime());
		existingEvent.setEndTime(eventDTO.getEndTime());
		existingEvent.setEventCoordinator(coordinator);

		// Save the updated event
		nonTechEventRepo.save(existingEvent);
		return true;
	}

	@Override
	public boolean deleteEvents() throws JavaPortalException {
		
		List<NonTechnicalEvent> listOfNonTechEvents = nonTechEventRepo.findByStatus(Status.CANCELLED);
		
		for (NonTechnicalEvent event : listOfNonTechEvents) {
			event.setEventCoordinator(null);
			nonTechEventRepo.delete(event);
		}
		
		return true;
	}
	
	@Override
	public List<NonTechnicalEventDTO> getEventsById(Integer empId) throws JavaPortalException {
		updateEventStatus();
		
		List<NonTechnicalEvent> listOfEvents = nonTechEventRepo.findByEventCoordinator_EmpId(empId);
		if (listOfEvents.isEmpty()) throw new JavaPortalException("Service.NO_EVENTS_FOUND");
		
		List<NonTechnicalEventDTO> listOfEventsDTO = new ArrayList<>();
		
		for (NonTechnicalEvent event : listOfEvents) {
			NonTechnicalEventDTO dto = new NonTechnicalEventDTO();
			dto.setNonTechnicalEventId(event.getNonTechnicalEventId());
			dto.setEventName(event.getEventName());
			dto.setLocation(event.getLocation());
			dto.setStatus(event.getStatus());
			dto.setDescription(event.getDescription());
			dto.setType(event.getType());
			dto.setStartTime(event.getStartTime());
			dto.setEndTime(event.getEndTime());
			dto.setEventCoordinator(event.getEventCoordinator().getEmpId());
			
			listOfEventsDTO.add(dto);
		}
		
		
		
		return listOfEventsDTO;
	}
	
	@Override
	public NonTechnicalEventDTO getEventByName(String eventName) throws JavaPortalException {
		Optional<NonTechnicalEvent> optNonTechEvent = nonTechEventRepo.findByEventName(eventName);
		NonTechnicalEvent event = optNonTechEvent.orElseThrow(() -> new JavaPortalException("Service.EVENT_NOT_FOUND"));
		
		NonTechnicalEventDTO dto = new NonTechnicalEventDTO();
		dto.setNonTechnicalEventId(event.getNonTechnicalEventId());
		dto.setEventName(event.getEventName());
		dto.setLocation(event.getLocation());
		dto.setStatus(event.getStatus());
		dto.setDescription(event.getDescription());
		dto.setType(event.getType());
		dto.setStartTime(event.getStartTime());
		dto.setEndTime(event.getEndTime());
		dto.setEventCoordinator(event.getEventCoordinator().getEmpId());
		dto.setEventCoordinator(event.getEventCoordinator().getEmpId());
		
		return dto;
	}
	
	
	public boolean updateEventStatus() throws JavaPortalException {
		List<NonTechnicalEvent> listOfInProgressEvents = nonTechEventRepo.findByStatus(Status.IN_PROGRESS);
		List<NonTechnicalEvent> listOfUpcomingEvents = nonTechEventRepo.findByStatus(Status.UPCOMING);
		
		for (NonTechnicalEvent t : listOfInProgressEvents) {
			LocalDateTime current = LocalDateTime.now();
			if (current.isAfter(t.getEndTime())) {
				t.setStatus(Status.COMPLETED);
				nonTechEventRepo.save(t);
			}
		}
		
		for (NonTechnicalEvent te : listOfUpcomingEvents) {
			LocalDateTime current = LocalDateTime.now();
			if (current.isAfter(te.getEndTime())) {
				te.setStatus(Status.COMPLETED);
				nonTechEventRepo.save(te);
			} else if (current.isAfter(te.getStartTime()) && current.isBefore(te.getEndTime())) {
				te.setStatus(Status.IN_PROGRESS);
				nonTechEventRepo.save(te);
			}
		}
		
		return true;
	}
	
	public boolean cancelEvent(String eventName) throws JavaPortalException {
		Optional<NonTechnicalEvent> optEvent = nonTechEventRepo.findByEventName(eventName);
		NonTechnicalEvent event = optEvent.orElseThrow(() -> new JavaPortalException("Service.EVENT_NOT_FOUND"));
		
		event.setStatus(Status.CANCELLED);
		
		nonTechEventRepo.save(event);
		
		return true;
	}
	
	@Override
	public List<NonTechnicalEventDTO> getAllEvents() throws JavaPortalException {
		updateEventStatus();
		
		List<NonTechnicalEvent> listOfEvents = nonTechEventRepo.findAll();
		
		List<NonTechnicalEventDTO> listOfEventsDTO = new ArrayList<>();
		
		for (NonTechnicalEvent event : listOfEvents) {
			NonTechnicalEventDTO dto = new NonTechnicalEventDTO();
			dto.setNonTechnicalEventId(event.getNonTechnicalEventId());
			dto.setEventName(event.getEventName());
			dto.setLocation(event.getLocation());
			dto.setStatus(event.getStatus());
			dto.setDescription(event.getDescription());
			dto.setType(event.getType());
			dto.setStartTime(event.getStartTime());
			dto.setEndTime(event.getEndTime());
			dto.setEventCoordinator(event.getEventCoordinator().getEmpId());
			
			listOfEventsDTO.add(dto);
		}
		
		return listOfEventsDTO;

	}

}

