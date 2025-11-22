package com.javaportal.service;

import java.util.List;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.dto.TeamDTO;
import com.javaportal.exception.JavaPortalException;

public interface TeamService {
	public List<TeamDTO> getAllTeams() throws JavaPortalException;
	public boolean deleteTeam(Integer teamId) throws JavaPortalException;
	public Integer createTeam(TeamDTO dto) throws JavaPortalException;
	public List<EmployeeDTO> getManagersWithoutTeams() throws JavaPortalException;
}
