package com.javaportal.service;

import java.util.List;

import com.javaportal.dto.TrainingEventDTO;
import com.javaportal.exception.JavaPortalException;

public interface OperationsAnchorService {
	public TrainingEventDTO getEventByName(String eventName) throws JavaPortalException;
	public Integer createTrainingEvent(TrainingEventDTO eventDTO) throws JavaPortalException;
	public boolean updateTrainingEvent(TrainingEventDTO eventDTO) throws JavaPortalException;
	public boolean deleteEvent() throws JavaPortalException;
	public List<TrainingEventDTO> getEventsById(Integer empId) throws JavaPortalException;
	public boolean updateEventStatus() throws JavaPortalException;
	public boolean cancelEvent(String eventName) throws JavaPortalException;
	public List<TrainingEventDTO> getAllEvents() throws JavaPortalException;
}
