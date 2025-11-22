package com.javaportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaportal.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer>{
	public Optional<Team> findByTeamName(String teamName);
}
