package com.javaportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javaportal.entity.Employee;
import com.javaportal.repository.EmployeeRepository;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {
	
	@Autowired
	private EmployeeRepository empRepo;
	
	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		Employee emp = empRepo.findByEmailId(emailId).orElseThrow(() -> new UsernameNotFoundException("Employee not found with email " + emailId));
		return new EmployeeUserDetails(emp);
	}
}
