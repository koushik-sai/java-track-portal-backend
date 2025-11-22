package com.javaportal.service;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.dto.LoginRequestDTO;
import com.javaportal.entity.Employee;
import com.javaportal.exception.JavaPortalException;

public interface LoginService {
	public EmployeeDTO getEmployee(LoginRequestDTO request) throws JavaPortalException;
	public Boolean updatePassword(String emailId, String password) throws JavaPortalException;
	public EmployeeDTO getEmployeeByEmailId(String emailId) throws JavaPortalException;
	public EmployeeDTO toDto(Employee employee);
}
