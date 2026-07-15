package com.param.kitchen.dao;

import java.util.List;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.kitchen.dto.RejectedDietListRequestDto;

public interface IRejectedDietListDao extends ICommonConstants {
	
	@SuppressWarnings("rawtypes")
	public Response getRejectedDietOrderListByMultipleFields(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId);
	
	@SuppressWarnings("rawtypes")
	public Response getRejectedDietListByOnlyDate(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId);
	
	@SuppressWarnings("rawtypes")
	public Response reIssueRejectedDietOrderByDietOrderId(List<Integer> orderIds);
	
	@SuppressWarnings("rawtypes")
	public Response getRejectedDietOrderListByMultipleFieldsWithoutMealId(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId);
	

}