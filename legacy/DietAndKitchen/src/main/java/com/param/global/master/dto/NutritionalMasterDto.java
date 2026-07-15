package com.param.global.master.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NutritionalMasterDto {

	private Integer nutritionId;

	private String nutritionCode;

	private String nutritionName;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Character status;

	private Integer organizationId;
	
	private Integer unitId;

	private double value;

	private Integer uomId;

	private String uomName;

	private Integer ingredientNutritionId;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;

	private String isCalories;

	public Integer getNutritionId() {
		return nutritionId;
	}

	public void setNutritionId(Integer nutritionId) {
		this.nutritionId = nutritionId;
	}

	public String getNutritionCode() {
		return nutritionCode;
	}

	public void setNutritionCode(String nutritionCode) {
		this.nutritionCode = nutritionCode;
	}

	public String getNutritionName() {
		return nutritionName;
	}

	public void setNutritionName(String nutritionName) {
		this.nutritionName = nutritionName;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public Integer getIngredientNutritionId() {
		return ingredientNutritionId;
	}

	public void setIngredientNutritionId(Integer ingredientNutritionId) {
		this.ingredientNutritionId = ingredientNutritionId;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOffset() {
		return offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getIsCalories() {
		return isCalories;
	}

	public void setIsCalories(String isCalories) {
		this.isCalories = isCalories;
	}
}
