package com.param.kitchen.dao;

import java.util.List;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.kitchen.dto.DietIssueDto;
import com.param.kitchen.dto.DietIssueRequestDto;
import com.param.kitchen.dto.DietOrderDto;

import in.param.exception.ApplicationException;

public interface IDietIssueDao extends ICommonConstants {
	
	@SuppressWarnings("rawtypes")
	public Response getDietIssueByMultipleFields(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId);
	
	@SuppressWarnings("rawtypes")
	public Response getDietIssueByOnlyDate(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId);
	
	@SuppressWarnings("rawtypes")
	public Response issueDietOrderByDietOrderList(List<Integer> dietOrderIdList);
	
	@SuppressWarnings("rawtypes")
	public Response updateStatus(DietIssueDto dietIssueDto);

	@SuppressWarnings("rawtypes")
	public Response getDietIssueByMultipleFieldsWithoutMealId(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId);
	
}
