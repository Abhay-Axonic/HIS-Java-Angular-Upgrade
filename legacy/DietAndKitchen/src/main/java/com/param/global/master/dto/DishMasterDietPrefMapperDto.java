package com.param.global.master.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DishMasterDietPrefMapperDto {

	Integer id;
	Integer dishId;
	Integer dietPreferenceId;
	Integer dietPrefId;
	String dietPreferenceCode;
	String dietPreferenceDesc;
	String mealPreferenceDesc;
	String dietTypeDescription;

	private Integer mealTypeId;
	private Integer dietTypeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}

	public Integer getDietPreferenceId() {
		return dietPreferenceId;
	}

	public void setDietPreferenceId(Integer dietPreferenceId) {
		this.dietPreferenceId = dietPreferenceId;
	}

	public Integer getDietPrefId() {
		return dietPrefId;
	}

	public void setDietPrefId(Integer dietPrefId) {
		this.dietPrefId = dietPrefId;
	}

	public String getDietPreferenceCode() {
		return dietPreferenceCode;
	}

	public void setDietPreferenceCode(String dietPreferenceCode) {
		this.dietPreferenceCode = dietPreferenceCode;
	}

	public String getDietPreferenceDesc() {
		return dietPreferenceDesc;
	}

	public void setDietPreferenceDesc(String dietPreferenceDesc) {
		this.dietPreferenceDesc = dietPreferenceDesc;
	}

	public Integer getMealTypeId() {
		return mealTypeId;
	}

	public void setMealTypeId(Integer mealTypeId) {
		this.mealTypeId = mealTypeId;
	}

	public String getMealPreferenceDesc() {
		return mealPreferenceDesc;
	}

	public void setMealPreferenceDesc(String mealPreferenceDesc) {
		this.mealPreferenceDesc = mealPreferenceDesc;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public String getDietTypeDescription() {
		return dietTypeDescription;
	}

	public void setDietTypeDescription(String dietTypeDescription) {
		this.dietTypeDescription = dietTypeDescription;
	}

}
