package com.param.kitchen.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DietOrderRequestDto {

	
	private String dietDate;
	
	private Integer wardId;
	
	private Integer floorId;
	
	private Integer mealId;

	public String getDietDate() {
		return dietDate;
	}

	public void setDietDate(String dietDate) {
		this.dietDate = dietDate;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
	}
	
	
	
	
}
