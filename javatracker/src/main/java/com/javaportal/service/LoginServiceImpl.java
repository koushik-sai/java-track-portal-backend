package com.javaportal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaportal.dto.EmployeeDTO;
import com.javaportal.dto.LoginRequestDTO;
import com.javaportal.entity.Employee;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    private final EmployeeRepository empRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(EmployeeRepository empRepo,PasswordEncoder passwordEncoder) {
        this.empRepo = empRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EmployeeDTO getEmployee(LoginRequestDTO request) throws JavaPortalException {
        Optional<Employee> optEmployee = empRepo.findByEmailId(request.getEmailId());
        Employee emp = optEmployee.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));

        EmployeeDTO edto = new EmployeeDTO();
        edto.setEmpId(emp.getEmpId());
        edto.setName(emp.getName());
        edto.setEmailId(emp.getEmailId());
        edto.setContact(emp.getContact());
        edto.setAddress(emp.getAddress());
        edto.setAreaOfInterest(emp.getAreaOfInterest());
        edto.setExpertise(emp.getExpertise());
        edto.setCabinDetails(emp.getCabinDetails());
        edto.setResponsibility(emp.getResponsibility());
        edto.setRole(emp.getRole());
        edto.setManagerId(emp.getManager() == null ? null : emp.getManager().getEmpId());
        edto.setTeamId(emp.getTeam() == null ? null : emp.getTeam().getTeamId());
        edto.setFirstLogin(emp.isFirstLogin());

        return edto;
    }

    @Override
    public EmployeeDTO getEmployeeByEmailId(String emailId) throws JavaPortalException {
        Optional<Employee> optEmployee = empRepo.findByEmailId(emailId);
        Employee emp = optEmployee.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));
        return toDto(emp);
    }

    @Transactional
    public EmployeeDTO toDto(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();

        dto.setEmpId(employee.getEmpId());
        dto.setName(employee.getName());
        dto.setEmailId(employee.getEmailId());
        dto.setContact(employee.getContact());
        dto.setAddress(employee.getAddress());

        List<String> interests = employee.getAreaOfInterest();
        dto.setAreaOfInterest(interests != null ? new ArrayList<>(interests) : Collections.emptyList());

        List<String> expertiseList = employee.getExpertise();
        dto.setExpertise(expertiseList != null ? new ArrayList<>(expertiseList) : Collections.emptyList());

        dto.setCabinDetails(employee.getCabinDetails());
        dto.setResponsibility(employee.getResponsibility());
        dto.setRole(employee.getRole());

        dto.setManagerId(employee.getManager() != null ? employee.getManager().getEmpId() : null);
        dto.setTeamId(employee.getTeam() != null ? employee.getTeam().getTeamId() : null);

        dto.setFirstLogin(employee.isFirstLogin());

        return dto;
    }

    @Override
    public Boolean updatePassword(String emailId, String password) throws JavaPortalException {
        Optional<Employee> optEmployee = empRepo.findByEmailId(emailId);

        Employee e = optEmployee.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));
        e.setPassword(passwordEncoder.encode(password));
        e.setFirstLogin(false);
        return true;
    }
}

