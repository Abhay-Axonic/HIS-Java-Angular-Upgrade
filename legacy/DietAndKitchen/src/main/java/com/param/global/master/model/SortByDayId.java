package com.param.global.master.model;

import java.util.Comparator;

import com.param.global.master.dto.MealsDto;

public class SortByDayId implements Comparator<MealsDto> {

	// Used for sorting in ascending order of
	// dayId
	public int compare(MealsDto a, MealsDto b) {
		return a.dayId - b.dayId;
	}

}
