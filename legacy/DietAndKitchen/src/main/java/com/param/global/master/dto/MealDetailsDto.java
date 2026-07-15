package com.param.global.master.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MealDetailsDto {

	private Integer mealDetailsId;

	private Integer dayId;
	
	private String dietDate;

	private Integer dietTemplateId;

	public Integer mealId;

	private Integer dishId;
	
	private Integer quantity;

	private String mealName;

	private String servingTime;
	
	private List<MealsDto> mealsList;
	
	private List<DishDto> dishList;

	private NutritionalValuesDto totalNutrition;
	
	private Character status;

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public String getServingTime() {
		return servingTime;
	}

	public void setServingTime(String servingTime) {
		this.servingTime = servingTime;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public Integer getDietTemplateId() {
		return dietTemplateId;
	}

	public void setDietTemplateId(Integer dietTemplateId) {
		this.dietTemplateId = dietTemplateId;
	}

	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
	}

	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<MealsDto> getMealsList() {
		return mealsList;
	}

	public void setMealsList(List<MealsDto> mealsList) {
		this.mealsList = mealsList;
	}

	public NutritionalValuesDto getTotalNutrition() {
		return totalNutrition;
	}

	public void setTotalNutrition(NutritionalValuesDto totalNutrition) {
		this.totalNutrition = totalNutrition;
	}

	public Integer getMealDetailsId() {
		return mealDetailsId;
	}

	public void setMealDetailsId(Integer mealDetailsId) {
		this.mealDetailsId = mealDetailsId;
	}

	public String getDietDate() {
		return dietDate;
	}

	public void setDietDate(String dietDate) {
		this.dietDate = dietDate;
	}

	public List<DishDto> getDishList() {
		return dishList;
	}

	public void setDishList(List<DishDto> dishList) {
		this.dishList = dishList;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
	

}
