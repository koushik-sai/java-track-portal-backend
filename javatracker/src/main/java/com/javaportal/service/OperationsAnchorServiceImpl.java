package com.javaportal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaportal.dto.TrainingEventDTO;
import com.javaportal.entity.Employee;
import com.javaportal.entity.Status;
import com.javaportal.entity.TrainingEvent;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;
import com.javaportal.repository.TrainingEventRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OperationsAnchorServiceImpl implements OperationsAnchorService {

	@Autowired
	private TrainingEventRepository trainingRepo;

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public TrainingEventDTO getEventByName(String eventName) throws JavaPortalException {
		Optional<TrainingEvent> optEvent = trainingRepo.findByEventName(eventName);
		TrainingEvent event = optEvent.orElseThrow(() -> new JavaPortalException("Service.EVENT_NOT_FOUND"));
		TrainingEventDTO dto = new TrainingEventDTO();
		dto.setEventName(event.getEventName());
		dto.setTrainingEventId(event.getTrainingEventId());
		dto.setDescription(event.getDescription());
		dto.setLocation(event.getLocation());
		dto.setStatus(event.getStatus());
		dto.setStartTime(event.getStartTime());
		dto.setEndTime(event.getEndTime());
		dto.setOperationsAnchor(event.getOperationsAnchor().getEmpId());

		return dto;
	}

	@Override
	public Integer createTrainingEvent(TrainingEventDTO eventDTO) throws JavaPortalException {
	    LocalDateTime now = LocalDateTime.now();

		if (trainingRepo.existsByEventName(eventDTO.getEventName())) {
			throw new JavaPortalException("Servie.EVENT_ALREADY_EXISTS");
		}

	    // Allow events starting now or in the future (even today)
	    if (eventDTO.getStartTime().isBefore(now) || eventDTO.getEndTime().isBefore(now)) {
	        throw new JavaPortalException("Service.EVENT_DATE_INVALID");
	    }

	    if (trainingRepo.existsByEventName(eventDTO.getEventName())) {
	        throw new JavaPortalException("Service.EVENT_ALREADY_EXISTS");
	    }

	    List<TrainingEvent> conflictingEvents = trainingRepo.findConflictingEvents(
	        eventDTO.getLocation(), eventDTO.getStartTime(), eventDTO.getEndTime());

	    if (!conflictingEvents.isEmpty()) {
	        throw new JavaPortalException("Service.EVENT_CONFLICT");
	    }

	    Employee anchor = empRepo.findById(eventDTO.getOperationsAnchor())
	        .orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));

	    TrainingEvent event = mapper.map(eventDTO, TrainingEvent.class);
	    event.setOperationsAnchor(anchor);
	    event.setStatus(Status.UPCOMING);

	    return trainingRepo.save(event).getTrainingEventId();
	}

	@Override
	public boolean updateTrainingEvent(TrainingEventDTO eventDTO) throws JavaPortalException {
		// Fetch the existing event
		TrainingEvent existingEvent = trainingRepo.findById(eventDTO.getTrainingEventId())
				.orElseThrow(() -> new JavaPortalException("Service.EVENT_NOT_FOUND"));

		// Check if start time is before end time
		if (eventDTO.getStartTime().isAfter(eventDTO.getEndTime())) {
			throw new JavaPortalException("Start time must be before end time.");
		}

		// Check for duplicate event name (excluding the current event ID)
		boolean isDuplicateName = trainingRepo.existsByEventNameAndTrainingEventIdNot(eventDTO.getEventName(),
				eventDTO.getTrainingEventId());
		if (isDuplicateName) {
			throw new JavaPortalException("Servie.EVENT_ALREADY_EXISTS");
		}

		// Check for conflicting events (same location and overlapping time, excluding
		// current event)
		List<TrainingEvent> conflictingEvents = trainingRepo.findConflictingEventsExcludingCurrent(
				eventDTO.getLocation(), eventDTO.getStartTime(), eventDTO.getEndTime(), eventDTO.getTrainingEventId());
		if (!conflictingEvents.isEmpty()) {
			throw new JavaPortalException("Service.EVENT_CONFLICT");
		}

		// Fetch the operations anchor (employee)
		Employee anchor = empRepo.findById(eventDTO.getOperationsAnchor())
				.orElseThrow(() -> new JavaPortalException("Operations anchor not found."));

		// Update fields
		existingEvent.setEventName(eventDTO.getEventName());
		existingEvent.setLocation(eventDTO.getLocation());
		existingEvent.setDescription(eventDTO.getDescription());
		existingEvent.setStartTime(eventDTO.getStartTime());
		existingEvent.setEndTime(eventDTO.getEndTime());
		existingEvent.setOperationsAnchor(anchor);

		// Save the updated event
		trainingRepo.save(existingEvent);
		return true;
	}

	@Override
	public boolean deleteEvent() throws JavaPortalException {

		List<TrainingEvent> listOfTrainingEvents = trainingRepo.findByStatus(Status.CANCELLED);

		for (TrainingEvent event : listOfTrainingEvents) {
			Integer eventId = event.getTrainingEventId();
			Optional<TrainingEvent> optEvent = trainingRepo.findById(eventId);
			TrainingEvent techEvent = optEvent.orElseThrow(() -> new JavaPortalException("Service.EVENT_NOT_FOUND"));

			techEvent.setOperationsAnchor(null);
			trainingRepo.save(techEvent);
			trainingRepo.delete(techEvent);
		}

		return true;
	}

	@Override
	public List<TrainingEventDTO> getEventsById(Integer empId) throws JavaPortalException {
		updateEventStatus();
		
		List<TrainingEvent> listOfEvents = trainingRepo.findByOperationsAnchor_EmpId(empId);

		if (listOfEvents.isEmpty())
			throw new JavaPortalException("Service.NO_EVENTS_FOUND");

		List<TrainingEventDTO> listOfEventsDTO = new ArrayList<>();

		for (TrainingEvent event : listOfEvents) {
			TrainingEventDTO dto = new TrainingEventDTO();
			dto.setEventName(event.getEventName());
			dto.setTrainingEventId(event.getTrainingEventId());
			dto.setDescription(event.getDescription());
			dto.setLocation(event.getLocation());
			dto.setStatus(event.getStatus());
			dto.setStartTime(event.getStartTime());
			dto.setEndTime(event.getEndTime());
			dto.setOperationsAnchor(event.getOperationsAnchor().getEmpId());
			
			

			listOfEventsDTO.add(dto);
		}

		return listOfEventsDTO;
	}
	
	
	public boolean updateEventStatus() throws JavaPortalException {
		List<TrainingEvent> listOfInProgressEvents = trainingRepo.findByStatus(Status.IN_PROGRESS);
		List<TrainingEvent> listOfUpcomingEvents = trainingRepo.findByStatus(Status.UPCOMING);
		
		for (TrainingEvent te : listOfInProgressEvents) {
			LocalDateTime current = LocalDateTime.now();
			if (current.isAfter(te.getEndTime())) {
				te.setStatus(Status.COMPLETED);
				trainingRepo.save(te);
			}
		}
		
		for (TrainingEvent te : listOfUpcomingEvents) {
			LocalDateTime current = LocalDateTime.now();
			if (current.isAfter(te.getEndTime()) ) {
				te.setStatus(Status.COMPLETED);
				trainingRepo.save(te);
			} else if (current.isAfter(te.getStartTime()) && current.isBefore(te.getEndTime())) te.setStatus(Status.IN_PROGRESS);
		}
		
		return true;
	}
	
	@Override
	public boolean cancelEvent(String eventName) throws JavaPortalException {
		Optional<TrainingEvent> optEvent = trainingRepo.findByEventName(eventName);
		TrainingEvent event = optEvent.orElseThrow(() -> new JavaPortalException("Service.EVENT_NOT_FOUND"));
		
		event.setStatus(Status.CANCELLED);
		
		trainingRepo.save(event);
		
		return true;
	}
	
	@Override
	public List<TrainingEventDTO> getAllEvents() throws JavaPortalException {
		updateEventStatus();
		List<TrainingEvent> listOfEvents = trainingRepo.findAll();
		
		List<TrainingEventDTO> listOfEventsDTO = new ArrayList<>();
		
		for (TrainingEvent event : listOfEvents) {
			TrainingEventDTO dto = new TrainingEventDTO();
			dto.setEventName(event.getEventName());
			dto.setTrainingEventId(event.getTrainingEventId());
			dto.setDescription(event.getDescription());
			dto.setLocation(event.getLocation());
			dto.setStatus(event.getStatus());
			dto.setStartTime(event.getStartTime());
			dto.setEndTime(event.getEndTime());
			dto.setOperationsAnchor(event.getOperationsAnchor().getEmpId());
			
			

			listOfEventsDTO.add(dto);
		}

		return listOfEventsDTO;
		
	}
}

