package com.javaportal.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamDTO {
	private Integer teamId;
	
	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(Integer teamSize) {
		this.teamSize = teamSize;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	@NotNull(message="{team.teamName.absent}")
	private String teamName;
	
	@NotNull(message="{team.description.absent}")
	private String description;
	
	@NotNull(message="{team.teamSize.absent}")
	@Min(value=2, message="{team.teamSize.min}")
	@Max(value=15, message="{team.teamSize.max}")
	private Integer teamSize;
	
	@NotNull(message="{team.manager.absent}")
	@Valid
	private Integer managerId;
}
