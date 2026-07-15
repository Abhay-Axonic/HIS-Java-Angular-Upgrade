package com.param.kitchen.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.master.dto.DishDto;
import com.param.kitchen.dto.DietAcceptanceRequestDto;
import com.param.kitchen.dto.PatientDetailsDto;

@SuppressWarnings("rawtypes")
public interface IDietAcceptanceService {
	
	/**
	 * This method returns the list of diet orders that are issued and ready for acceptance.	
	 * @param dietAcceptanceRequestDto
	 * @return Response
	 */
	@Transactional
	public Response getDietAcceptanceByMultipleFields(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId );
	
	
	@Transactional
	public Response getDietAcceptanceByOnlyDate(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId);
	
	
	/**
	 * This method is used to accept the diet order by diet order id.  
	 * @param dietOrderId
	 * @return Response
	 */
	
	@Transactional
	public Response acceptDietOrderByDietOrderId(List<PatientDetailsDto> patientDetailsList);
	
	/**
	 * This method is used to reject the diet order by diet order id.
	 * @param dietOrderId
	 * @param remarks
	 * @return Response
	 */
	
	@Transactional
	public Response rejectDietOrderByDietOrderId(List<DishDto> orderList, String remarks);
}
