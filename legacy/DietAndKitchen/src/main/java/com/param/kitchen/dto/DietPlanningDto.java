package com.param.kitchen.dto;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.master.dto.MealDetailsDto;

@JsonInclude(Include.NON_NULL)
public class DietPlanningDto {

	private Integer dietPlanningId;

	private Integer patientId;
	
	private Integer dietTypeId;
	
	private Integer dietPreferenceId;
	
	private String dietTypeDesc;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private List<MealDetailsDto> mealDetailsList;

	private Character status;

	public Integer getDietPlanningId() {
		return dietPlanningId;
	}

	public void setDietPlanningId(Integer dietPlanningId) {
		this.dietPlanningId = dietPlanningId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public String getDietTypeDesc() {
		return dietTypeDesc;
	}

	public void setDietTypeDesc(String dietTypeDesc) {
		this.dietTypeDesc = dietTypeDesc;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public List<MealDetailsDto> getMealDetailsList() {
		return mealDetailsList;
	}

	public void setMealDetailsList(List<MealDetailsDto> mealDetailsList) {
		this.mealDetailsList = mealDetailsList;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getDietPreferenceId() {
		return dietPreferenceId;
	}

	public void setDietPreferenceId(Integer dietPreferenceId) {
		this.dietPreferenceId = dietPreferenceId;
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
	
	
	
	
	
		
}
