package com.javaportal.service;

import java.util.List;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.exception.JavaPortalException;

public interface ManagerService {
	public boolean addMemberToTeam(Integer managerId, Integer empId) throws JavaPortalException;
	public boolean updateMember(EmployeeDTO empDTO) throws JavaPortalException;
	public boolean removeFromTeam(Integer empId) throws JavaPortalException;
	public EmployeeDTO getEmployeeDetails(Integer empId) throws JavaPortalException;
	public List<EmployeeDTO> getEmployeeByManagerId(Integer managerId) throws JavaPortalException;
	public List<EmployeeDTO> getEmployeeNotInAnyTeam() throws JavaPortalException;
}
