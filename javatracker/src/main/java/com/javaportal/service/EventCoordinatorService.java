package com.javaportal.service;

import java.util.List;

import com.javaportal.dto.NonTechnicalEventDTO;
import com.javaportal.exception.JavaPortalException;

public interface EventCoordinatorService {
	public List<NonTechnicalEventDTO> getEventsById(Integer empId) throws JavaPortalException;
	public Integer createEvent(NonTechnicalEventDTO nonTechEventDTO) throws JavaPortalException;
	public boolean updateEvent(NonTechnicalEventDTO nonTechEventDTO) throws JavaPortalException;
	public boolean deleteEvents() throws JavaPortalException;
	public NonTechnicalEventDTO getEventByName(String eventName) throws JavaPortalException;
	public boolean cancelEvent(String eventName) throws JavaPortalException;
	public List<NonTechnicalEventDTO> getAllEvents() throws JavaPortalException;
}
