package com.javaportal.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaportal.entity.Thoughts;

public interface ThoughtsRepository extends JpaRepository<Thoughts, Integer>{
	public List<Thoughts> findByEmployee_EmpId(Integer empId);
	public List<Thoughts> findBySharedOnGreaterThanEqual(LocalDateTime sharedOn);
}
