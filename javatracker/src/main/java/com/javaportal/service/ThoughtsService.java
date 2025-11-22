package com.javaportal.service;

import java.util.List;

import com.javaportal.dto.ThoughtsDTO;
import com.javaportal.exception.JavaPortalException;

public interface ThoughtsService {
	public List<ThoughtsDTO> getAllThoughts(Integer empId) throws JavaPortalException;
	public boolean addThought(ThoughtsDTO thoughtDTO) throws JavaPortalException;
	public boolean deleteThought(Integer thoughtId) throws JavaPortalException;
	public List<ThoughtsDTO> getFromLastTwoDays() throws JavaPortalException;
}
