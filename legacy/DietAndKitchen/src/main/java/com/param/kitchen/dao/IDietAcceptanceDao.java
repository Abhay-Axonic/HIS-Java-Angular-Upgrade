package com.param.kitchen.dao;

import java.util.List;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.master.dto.DishDto;
import com.param.kitchen.dto.DietAcceptanceRequestDto;
import com.param.kitchen.dto.PatientDetailsDto;

public interface IDietAcceptanceDao extends ICommonConstants {
	
	@SuppressWarnings("rawtypes")
	public Response acceptDietOrderByDietOrderId(List<PatientDetailsDto> patientDetailsList);
	
	@SuppressWarnings("rawtypes")
	public Response rejectDietOrderByDietOrderId(List<DishDto> orderList, String remarks);
	
	@SuppressWarnings("rawtypes")
	public Response getDietAcceptanceByMultipleFields(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId);
	
	@SuppressWarnings("rawtypes")
	public Response getDietAcceptanceByOnlyDate(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId);
	
	
	@SuppressWarnings("rawtypes")
	public Response getDietAcceptanceByTransitionWithouMealId(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId);
	
	
	@SuppressWarnings("rawtypes")
	public Response getDietAcceptanceByMealIdWithouTransition(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId);
	
	
	
	
}
		