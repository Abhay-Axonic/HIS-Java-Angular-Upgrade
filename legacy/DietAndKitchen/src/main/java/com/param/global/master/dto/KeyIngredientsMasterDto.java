package com.param.global.master.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class KeyIngredientsMasterDto {

	private Integer keyIngredientId;

	private Integer organizationId;
	
	private Integer unitId;

	private String ingredientCode;

	private String ingredientDescription;

	private Double ingredientValue;

	private Integer uomId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Character status;

	private Integer nutritionId;

	private Double nutritionValue;

	private String uomName;

	private Integer value; //dilip

	private Integer ingredient_nutrition_id;

	private Integer dishMasterKeyIngredientsMapperId;

	private List<NutritionalMasterDto> nutriciansList;

	public Integer getKeyIngredientId() {
		return keyIngredientId;
	}

	public void setKeyIngredientId(Integer keyIngredientId) {
		this.keyIngredientId = keyIngredientId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getIngredientCode() {
		return ingredientCode;
	}

	public void setIngredientCode(String ingredientCode) {
		this.ingredientCode = ingredientCode;
	}

	public String getIngredientDescription() {
		return ingredientDescription;
	}

	public void setIngredientDescription(String ingredientDescription) {
		this.ingredientDescription = ingredientDescription;
	}

	public Double getIngredientValue() {
		return ingredientValue;
	}

	public void setIngredientValue(Double ingredientValue) {
		this.ingredientValue = ingredientValue;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
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

	public List<NutritionalMasterDto> getNutriciansList() {
		return nutriciansList;
	}

	public void setNutriciansList(List<NutritionalMasterDto> nutriciansList) {
		this.nutriciansList = nutriciansList;
	}

	public Integer getNutritionId() {
		return nutritionId;
	}

	public void setNutritionId(Integer nutritionId) {
		this.nutritionId = nutritionId;
	}

	public Double getNutritionValue() {
		return nutritionValue;
	}

	public void setNutritionValue(Double nutritionValue) {
		this.nutritionValue = nutritionValue;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public Integer getIngredient_nutrition_id() {
		return ingredient_nutrition_id;
	}

	public void setIngredient_nutrition_id(Integer ingredient_nutrition_id) {
		this.ingredient_nutrition_id = ingredient_nutrition_id;
	}

	public Integer getDishMasterKeyIngredientsMapperId() {
		return dishMasterKeyIngredientsMapperId;
	}

	public void setDishMasterKeyIngredientsMapperId(Integer dishMasterKeyIngredientsMapperId) {
		this.dishMasterKeyIngredientsMapperId = dishMasterKeyIngredientsMapperId;
	}

	//dilip
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	
	
	

}
