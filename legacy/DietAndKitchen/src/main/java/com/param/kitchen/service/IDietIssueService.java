package com.param.kitchen.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.kitchen.dto.DietIssueDto;
import com.param.kitchen.dto.DietIssueRequestDto;
import com.param.kitchen.dto.DietOrderDto;

@SuppressWarnings("rawtypes")
public interface IDietIssueService {
	
	
	/**
	 * This method returns the list of diet orders that are available to issue.
	 * @param dietIssueRequestDto
	 * @return Response
	 */
	@Transactional
	public Response getDietIssueByMultipleFields(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId);	
	
	
	@Transactional
	public Response getDietIssueByOnlyDate(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId);	
	
	
	/** 
	 * This method is used to issue a particular diet order by the diet order id.
	 * @param dietOrderId
	 * @return Response
	 */
	@Transactional
	public Response issueDietOrderByDietOrderList(List<Integer> dietOrderIdList);
	
	@Transactional
	public Response updateStatus(DietIssueDto dietIssueDto);
}
