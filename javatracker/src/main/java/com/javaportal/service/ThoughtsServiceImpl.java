package com.javaportal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaportal.dto.ThoughtsDTO;
import com.javaportal.entity.Employee;
import com.javaportal.entity.Thoughts;
import com.javaportal.exception.JavaPortalException;
import com.javaportal.repository.EmployeeRepository;
import com.javaportal.repository.ThoughtsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ThoughtsServiceImpl implements ThoughtsService {
	
	@Autowired
	private ThoughtsRepository thoughtsRepo;
	
	@Autowired
	private EmployeeRepository empRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<ThoughtsDTO> getFromLastTwoDays() throws JavaPortalException {
		LocalDateTime targetDate = LocalDateTime.now().minusDays(2);
		List<Thoughts> listOfThoughts = thoughtsRepo.findBySharedOnGreaterThanEqual(targetDate);
		List<ThoughtsDTO> listOfDtos = new ArrayList<>();
		
		for (Thoughts t : listOfThoughts) {
			ThoughtsDTO dto = new ThoughtsDTO();
			dto.setThoughtId(t.getThoughtId());
			dto.setEmpId(t.getEmployee().getEmpId());
			dto.setEmpName(t.getEmpName());
			dto.setMessage(t.getMessage());
			dto.setSharedOn(t.getSharedOn());
			
			listOfDtos.add(dto);
		}
		
		return listOfDtos;
	}
	
	@Override
	public List<ThoughtsDTO> getAllThoughts(Integer empId) throws JavaPortalException {
		List<Thoughts> listOfThoughts = thoughtsRepo.findByEmployee_EmpId(empId);
		
		List<ThoughtsDTO> listOfDtos = new ArrayList<>();
		
		for (Thoughts t : listOfThoughts) {
			ThoughtsDTO dto = new ThoughtsDTO();
			dto.setThoughtId(t.getThoughtId());
			dto.setEmpId(t.getEmployee().getEmpId());
			dto.setEmpName(t.getEmpName());
			dto.setMessage(t.getMessage());
			dto.setSharedOn(t.getSharedOn());
			
			listOfDtos.add(dto);
		}
		
		listOfDtos.sort((a, b) -> b.getSharedOn().compareTo(a.getSharedOn()));
		return listOfDtos;
	}

	@Override
	public boolean addThought(ThoughtsDTO thoughtDTO) throws JavaPortalException {
		Optional<Employee> optEmp = empRepo.findById(thoughtDTO.getEmpId());
		Employee emp = optEmp.orElseThrow(() -> new JavaPortalException("Service.EMPLOYEE_NOT_FOUND"));
		
		Thoughts thought = modelMapper.map(thoughtDTO, Thoughts.class);
		thought.setEmployee(emp);
		thought.setSharedOn(LocalDateTime.now());
		
		thoughtsRepo.save(thought);
		
		return true;
	}

	@Override
	public boolean deleteThought(Integer thoughtId) throws JavaPortalException {	
		Optional<Thoughts> optThought = thoughtsRepo.findById(thoughtId);
		Thoughts thought = optThought.orElseThrow(() -> new JavaPortalException("Service.THOUGHT_NOT_FOUND"));
		
		thought.setEmployee(null);
		
		thoughtsRepo.delete(thought);
		
		return true;
	}
	
	
}

