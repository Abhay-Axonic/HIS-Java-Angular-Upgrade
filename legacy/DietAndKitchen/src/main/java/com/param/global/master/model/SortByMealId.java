package com.param.global.master.model;

import java.util.Comparator;

import com.param.global.master.dto.MealDetailsDto;

public class SortByMealId implements Comparator<MealDetailsDto> {

	// Used for sorting in ascending order of
	// dayId
	public int compare(MealDetailsDto a, MealDetailsDto b) {
		return a.mealId - b.mealId;
	}

}
