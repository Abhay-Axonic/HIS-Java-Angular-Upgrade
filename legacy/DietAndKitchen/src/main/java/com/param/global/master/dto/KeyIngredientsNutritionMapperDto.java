package com.param.global.master.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class KeyIngredientsNutritionMapperDto {

	private Integer ingredientNutritionId;

	private Integer keyIngredientsMasterId;

	private Integer nutritionMasterId;

	private Integer organizationId;

	private Double nutritionValue;

	private Integer unitOfMeasurmentId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Character status;

	public int getIngredientNutritionId() {
		return ingredientNutritionId;
	}

	public void setIngredientNutritionId(int ingredientNutritionId) {
		this.ingredientNutritionId = ingredientNutritionId;
	}

	public int getKeyIngredientsMasterId() {
		return keyIngredientsMasterId;
	}

	public void setKeyIngredientsMasterId(int keyIngredientsMasterId) {
		this.keyIngredientsMasterId = keyIngredientsMasterId;
	}

	public int getNutritionMasterId() {
		return nutritionMasterId;
	}

	public void setNutritionMasterId(int nutritionMasterId) {
		this.nutritionMasterId = nutritionMasterId;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public double getNutritionValue() {
		return nutritionValue;
	}

	public void setNutritionValue(double nutritionValue) {
		this.nutritionValue = nutritionValue;
	}

	public Integer getUnitOfMeasurment() {
		return unitOfMeasurmentId;
	}

	public void setUnitOfMeasurment(Integer unitOfMeasurment) {
		this.unitOfMeasurmentId = unitOfMeasurment;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

}
