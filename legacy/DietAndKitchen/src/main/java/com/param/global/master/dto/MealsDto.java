package com.param.global.master.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class MealsDto {

	private Integer mealDetailsId;

	private Integer mealId;

	public Integer dayId;
	
	private String dietDate;

	private String mealName;

	@JsonProperty("isExpiredMealTime")
	private boolean isExpiredMealTime;

	private boolean expiredMealTime;

	private List<DishDto> dishList;

	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public List<DishDto> getDishList() {
		return dishList;
	}

	public void setDishList(List<DishDto> dishList) {
		this.dishList = dishList;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
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

	public boolean isExpiredMealTime() {
		return isExpiredMealTime;
	}

	public void setExpiredMealTime(boolean expiredMealTime) {
		this.isExpiredMealTime = expiredMealTime;
	}

}
