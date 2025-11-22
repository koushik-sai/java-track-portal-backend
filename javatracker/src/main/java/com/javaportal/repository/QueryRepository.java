package com.javaportal.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaportal.entity.Query;

public interface QueryRepository extends JpaRepository<Query, Integer>{
	public List<Query> findBySentOnGreaterThanEqual(LocalDateTime sentOn);
}
