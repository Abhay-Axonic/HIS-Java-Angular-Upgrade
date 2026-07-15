package com.param.global.master.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DishMasterDto {

	Integer dishId;

	String dishCode;

	String dishDescription;

	Double dishValue;

	Integer uomId;

	Integer quantity;

	String uomName;

	Integer dietPreferenceId;

	Integer dietPrefId;

	Integer mealPreferenceId;

	Integer dietTypeId;

	List<DishMasterDietPrefMapperDto> dietPreferenceList;
	
	List<DishMasterDietPrefMapperDto> dietMealTypeList;
	
	List<DishMasterDietPrefMapperDto> dietTypeList;
 
	String dietPreference;

	String dietPreferenceDesc;

	Integer createdBy;

	Integer updatedBy;

	String createdDate;

	String updatedDate;

	Character status;
	
	Integer mealDetailsId;
	
	Integer organizationId;
	
	Integer unitId;
	String mealNotes;
	private List<KeyIngredientsMasterDto> ingredientsList;

	public Integer getMealDetailsId() {
		return mealDetailsId;
	}

	public void setMealDetailsId(Integer mealDetailsId) {
		this.mealDetailsId = mealDetailsId;
	}


	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}

	public String getDishCode() {
		return dishCode;
	}

	public void setDishCode(String dishCode) {
		this.dishCode = dishCode;
	}

	public String getDishDescription() {
		return dishDescription;
	}

	public void setDishDescription(String dishDescription) {
		this.dishDescription = dishDescription;
	}

	public Double getDishValue() {
		return dishValue;
	}

	public void setDishValue(Double dishValue) {
		this.dishValue = dishValue;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public List<KeyIngredientsMasterDto> getIngredientsList() {
		return ingredientsList;
	}

	public void setIngredientsList(List<KeyIngredientsMasterDto> ingredientsList) {
		this.ingredientsList = ingredientsList;
	}

	public String getDietPreference() {
		return dietPreference;
	}

	public void setDietPreference(String dietPreference) {
		this.dietPreference = dietPreference;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public Integer getDietPreferenceId() {
		return dietPreferenceId;
	}

	public void setDietPreferenceId(Integer dietPreferenceId) {
		this.dietPreferenceId = dietPreferenceId;
	}

	public String getDietPreferenceDesc() {
		return dietPreferenceDesc;
	}

	public void setDietPreferenceDesc(String dietPreferenceDesc) {
		this.dietPreferenceDesc = dietPreferenceDesc;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<DishMasterDietPrefMapperDto> getDietPreferenceList() {
		return dietPreferenceList;
	}

	public void setDietPreferenceList(List<DishMasterDietPrefMapperDto> dietPreferenceList) {
		this.dietPreferenceList = dietPreferenceList;
	}

	public Integer getDietPrefId() {
		return dietPrefId;
	}

	public void setDietPrefId(Integer dietPrefId) {
		this.dietPrefId = dietPrefId;
	}

	public Integer getMealPreferenceId() {
		return mealPreferenceId;
	}

	public void setMealPreferenceId(Integer mealPreferenceId) {
		this.mealPreferenceId = mealPreferenceId;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
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

	public String getMealNotes() {
		return mealNotes;
	}

	public void setMealNotes(String mealNotes) {
		this.mealNotes = mealNotes;
	}

	public List<DishMasterDietPrefMapperDto> getDietMealTypeList() {
		return dietMealTypeList;
	}

	public void setDietMealTypeList(List<DishMasterDietPrefMapperDto> dietMealTypeList) {
		this.dietMealTypeList = dietMealTypeList;
	}

	public List<DishMasterDietPrefMapperDto> getDietTypeList() {
		return dietTypeList;
	}

	public void setDietTypeList(List<DishMasterDietPrefMapperDto> dietTypeList) {
		this.dietTypeList = dietTypeList;     
	}



	

	
}
