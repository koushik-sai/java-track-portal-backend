package com.javaportal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaportal.entity.Employee;
import com.javaportal.entity.Role;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	public List<Employee> findByRoleAndTeamIsNull(Role role);
	public Optional<Employee> findByEmailId(String emailId);
	public List<Employee> findByTeam_TeamId(Integer teamId);
	public List<Employee> findByManager_EmpId(Integer managerId); 
	public List<Employee> findByManagerIsNullAndTeamIsNull();
}
