package com.param.global.master.dto;

import java.util.List;

public class DayWiseNutritionValuesDto {

	Integer dayId;
	List<NutritionalValuesDto> nutritionList;

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public List<NutritionalValuesDto> getNutritionList() {
		return nutritionList;
	}

	public void setNutritionList(List<NutritionalValuesDto> nutritionList) {
		this.nutritionList = nutritionList;
	}

	

}
