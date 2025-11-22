package com.javaportal.service;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.exception.JavaPortalException;

public interface TeamMemberService {
	public EmployeeDTO getEmployeeById(Integer empId) throws JavaPortalException;
	public boolean updateEmployeeDetails(EmployeeDTO dto) throws JavaPortalException;
}
