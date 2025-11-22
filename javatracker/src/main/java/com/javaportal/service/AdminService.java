package com.javaportal.service;

import java.util.List;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.exception.JavaPortalException;

public interface AdminService {
	
	public List<EmployeeDTO> getEmployeesByTeam(String teamName) throws JavaPortalException;
	public EmployeeDTO getEmployee(Integer empId) throws JavaPortalException;
	public Integer createEmployee(EmployeeDTO empDTO) throws JavaPortalException;
	public boolean updateEmployee(EmployeeDTO empDTO) throws JavaPortalException;
	public Integer deleteEmployee(Integer empId) throws JavaPortalException;
	public List<String> getAllTeamNames() throws JavaPortalException;
}
