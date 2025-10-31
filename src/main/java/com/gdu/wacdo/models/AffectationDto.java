package com.gdu.wacdo.models;

import java.util.Date;

import jakarta.validation.constraints.NotNull;

public class AffectationDto {

	private Long id;

	@NotNull(message = "Le collaborateur est obligatoire")
	private Long collaboratorId;

	@NotNull(message = "Le restaurant est obligatoire")
	private Long restaurantId;

	@NotNull(message = "Le poste est obligatoire")
	private Long jobId;

	private Date startDate;
	private Date endDate;
	private Date createdAt;
	private Date updatedAt;

	public AffectationDto() {
	}

	public AffectationDto(Long id, Long collaboratorId, Long restaurantId, Long jobId, Date startDate, Date endDate,
			Date createdAt, Date updatedAt) {
		this.id = id;
		this.collaboratorId = collaboratorId;
		this.restaurantId = restaurantId;
		this.jobId = jobId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Getters et Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCollaboratorId() {
		return collaboratorId;
	}

	public void setCollaboratorId(Long collaboratorId) {
		this.collaboratorId = collaboratorId;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}