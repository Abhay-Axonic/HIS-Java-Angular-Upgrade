package com.param.global.unit.dto;

public class GrowthDevelopmentalMilestonesDto {
	
	private Integer growthDevelopmentalMilestonesTransactionId;
	
	private Integer growthDevelopmentalMilestonesId;
	
	private Integer ageFrom;
	
	private Integer ageTo;
	
	private String type;
	
	private String milestone;
	
	private String achievedDate;
	
	private Integer actualAgeOfMilestoneAchieved;

	private Integer patientId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;
	
	private boolean selected;

	public Integer getGrowthDevelopmentalMilestonesTransactionId() {
		return growthDevelopmentalMilestonesTransactionId;
	}

	public void setGrowthDevelopmentalMilestonesTransactionId(Integer growthDevelopmentalMilestonesTransactionId) {
		this.growthDevelopmentalMilestonesTransactionId = growthDevelopmentalMilestonesTransactionId;
	}

	public Integer getGrowthDevelopmentalMilestonesId() {
		return growthDevelopmentalMilestonesId;
	}

	public void setGrowthDevelopmentalMilestonesId(Integer growthDevelopmentalMilestonesId) {
		this.growthDevelopmentalMilestonesId = growthDevelopmentalMilestonesId;
	}

	public Integer getAgeFrom() {
		return ageFrom;
	}

	public void setAgeFrom(Integer ageFrom) {
		this.ageFrom = ageFrom;
	}

	public Integer getAgeTo() {
		return ageTo;
	}

	public void setAgeTo(Integer ageTo) {
		this.ageTo = ageTo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMilestone() {
		return milestone;
	}

	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}

	public String getAchievedDate() {
		return achievedDate;
	}

	public void setAchievedDate(String achievedDate) {
		this.achievedDate = achievedDate;
	}

	public Integer getActualAgeOfMilestoneAchieved() {
		return actualAgeOfMilestoneAchieved;
	}

	public void setActualAgeOfMilestoneAchieved(Integer actualAgeOfMilestoneAchieved) {
		this.actualAgeOfMilestoneAchieved = actualAgeOfMilestoneAchieved;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "GrowthDevelopmentalMilestonesDto [growthDevelopmentalMilestonesTransactionId="
				+ growthDevelopmentalMilestonesTransactionId + ", growthDevelopmentalMilestonesId="
				+ growthDevelopmentalMilestonesId + ", ageFrom=" + ageFrom + ", ageTo=" + ageTo + ", type=" + type
				+ ", milestone=" + milestone + ", achievedDate=" + achievedDate + ", actualAgeOfMilestoneAchieved="
				+ actualAgeOfMilestoneAchieved + ", patientId=" + patientId + ", organizationId=" + organizationId
				+ ", unitId=" + unitId + ", status=" + status + ", createdDate=" + createdDate + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + "]";
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	
}
