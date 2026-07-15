package com.param.global.dto;

public class NewBornApgarDto {
	
	private int assessmentId;

	private int apgarId;
	
	private String colorScore;

	private String heartRateScore;
	
	private String reflexScore;
	
	private String muscleScore;
	
	private String respirationScore;
	
	private int time;
	
	private String apgarRemark;
	
	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private Integer unitId;
	
	private int totalScore;
	
	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public int getApgarId() {
		return apgarId;
	}

	public void setApgarId(int apgarId) {
		this.apgarId = apgarId;
	}

	public String getColorScore() {
		return colorScore;
	}

	public void setColorScore(String colorScore) {
		this.colorScore = colorScore;
	}

	public String getHeartRateScore() {
		return heartRateScore;
	}

	public void setHeartRateScore(String heartRateScore) {
		this.heartRateScore = heartRateScore;
	}

	public String getReflexScore() {
		return reflexScore;
	}

	public void setReflexScore(String reflexScore) {
		this.reflexScore = reflexScore;
	}

	public String getMuscleScore() {
		return muscleScore;
	}

	public void setMuscleScore(String muscleScore) {
		this.muscleScore = muscleScore;
	}

	public String getRespirationScore() {
		return respirationScore;
	}

	public void setRespirationScore(String respirationScore) {
		this.respirationScore = respirationScore;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getApgarRemark() {
		return apgarRemark;
	}

	public void setApgarRemark(String apgarRemark) {
		this.apgarRemark = apgarRemark;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}



}
