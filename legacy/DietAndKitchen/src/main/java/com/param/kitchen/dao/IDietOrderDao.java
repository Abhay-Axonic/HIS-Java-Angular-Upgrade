package com.param.kitchen.dao;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.kitchen.dto.DietOrderRequestDto;

public interface IDietOrderDao extends ICommonConstants {
	
	
	@SuppressWarnings("rawtypes")
	public Response getDietOrderByMultipleFields(DietOrderRequestDto dietOrderRequestDto,Integer organizationId,Integer unitId);

	@SuppressWarnings("rawtypes")
	public Response getPatientDetailsByPatientId(Integer patientId,Integer organizationId,Integer unitId);

}
