package com.param.global.master.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.master.dto.MealDetailsDto;

@JsonInclude(Include.NON_NULL)
public class DietTemplateDto {

	private Integer dietTemplateId;

	private Integer dietTypeId;

	private String dietTypeDesc;

	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;

	private Integer updatedBy;

	private String createdDate;

	private String updatedDate;

	private List<DietTemplateDietPreferenceMapperDto> dietPreferenceList;

	private List<MealDetailsDto> mealDetailsList;

	public Integer getDietTemplateId() {
		return dietTemplateId;
	}

	public void setDietTemplateId(Integer dietTemplateId) {
		this.dietTemplateId = dietTemplateId;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<DietTemplateDietPreferenceMapperDto> getDietPreferenceList() {
		return dietPreferenceList;
	}

	public void setDietPreferenceList(List<DietTemplateDietPreferenceMapperDto> dietPreferenceList) {
		this.dietPreferenceList = dietPreferenceList;
	}

	public List<MealDetailsDto> getMealDetailsList() {
		return mealDetailsList;
	}

	public void setMealDetailsList(List<MealDetailsDto> mealDetailsList) {
		this.mealDetailsList = mealDetailsList;
	}

	public String getDietTypeDesc() {
		return dietTypeDesc;
	}

	public void setDietTypeDesc(String dietTypeDesc) {
		this.dietTypeDesc = dietTypeDesc;
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
