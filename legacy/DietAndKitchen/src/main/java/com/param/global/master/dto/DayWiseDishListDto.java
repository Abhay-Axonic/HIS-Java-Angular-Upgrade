package com.param.global.master.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DayWiseDishListDto {

	private Integer dayId;
	private List<DishDto> dishList;

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public List<DishDto> getDishList() {
		return dishList;
	}

	public void setDishList(List<DishDto> dishList) {
		this.dishList = dishList;
	}

}
