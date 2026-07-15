package com.param.kitchen.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.kitchen.dto.DietOrderRequestDto;

@SuppressWarnings("rawtypes")
public interface IDietOrderService {
	
	/**
	 * This method returns the list of orders for particular meal type for particular date.
	 * @param dietOrderRequestDto
	 * @return Response
	 */
	@Transactional
	public Response getDietOrderByMultipleFields(DietOrderRequestDto dietOrderRequestDto,Integer organizationId,Integer unitId);	
	
	@Transactional
	public Response getPatientDetailsByPatientId(Integer patientId,Integer organizationId,Integer unitId);
}


