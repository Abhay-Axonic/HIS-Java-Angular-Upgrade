package com.param.global.master.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DishDto {

	private Integer dishId;

	private String dishName;
	
	private String dishDescription;

	private Integer quantity;

	private Integer mealDetailsId;
	
	private Integer orderId;
	private String mealNotes;

//	@JsonProperty("isExpiredMealTime")
	private boolean isExpiredMealTime;

	List<DishMasterDietPrefMapperDto> dietPreferenceList;

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

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public Integer getMealDetailsId() {
		return mealDetailsId;
	}

	public void setMealDetailsId(Integer mealDetailsId) {
		this.mealDetailsId = mealDetailsId;
	}

	public String getDishDescription() {
		return dishDescription;
	}

	public void setDishDescription(String dishDescription) {
		this.dishDescription = dishDescription;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public boolean isExpiredMealTime() {
		return isExpiredMealTime;
	}

	public void setExpiredMealTime(boolean expiredMealTime) {
		isExpiredMealTime = expiredMealTime;
	}

	public List<DishMasterDietPrefMapperDto> getDietPreferenceList() {
		return dietPreferenceList;
	}

	public void setDietPreferenceList(List<DishMasterDietPrefMapperDto> dietPreferenceList) {
		this.dietPreferenceList = dietPreferenceList;
	}

	public String getMealNotes() {
		return mealNotes;
	}

	public void setMealNotes(String mealNotes) {
		this.mealNotes = mealNotes;
	}
	

}
