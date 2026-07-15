package com.param.kitchen.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.kitchen.dto.RejectedDietListRequestDto;

@SuppressWarnings("rawtypes")
public interface IRejectedDietListService {

	
	
	@Transactional
	public Response getRejectedDietListByMultipleFields(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId);
	
	@Transactional
	public Response getRejectedDietListByOnlyDate(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId);
	
	@Transactional
	public Response reIssueRejectedDietOrderByDietOrderId(List<Integer> orderIds);
}
