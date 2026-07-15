package com.param.global.doctor.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.doctor.dto.OrderSetReqDto;
import com.param.global.doctor.dto.OrdersetCreationReqDto;
import com.param.global.doctor.dto.OrdersetMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOrdersetMasterService {
	public Response createOrderset(OrdersetCreationReqDto reqDto)throws ApplicationException;
	public Response getOrderSet(OrderSetReqDto reqDto)throws ApplicationException;
	public Response getOrderSetOrders(List<OrderSetReqDto> list)throws ApplicationException;
	
	public Response getInvestigationByOrderSet(OrdersetMasterDto reqDto)throws ApplicationException;
	public Response getMedicationByOrderset(OrdersetMasterDto reqDto)throws ApplicationException;
	
	public Response getOrderSetsList(OrdersetMasterDto dto)throws ApplicationException;
	public Response getOrderSetsListCount(OrdersetMasterDto dto)throws ApplicationException;
	public Response orderSetsDetails(OrdersetMasterDto dto)throws ApplicationException;
	public Response changeStatusOrderSet(Integer ordersetMasterId,Character status)throws ApplicationException;
	public Response orderSetSearch(int unitId,String searchkeyWord,int orderSetActiveTab)throws ApplicationException;
	@Transactional
	public Response updateOrderSetsDetails(OrdersetCreationReqDto reqDto)throws ApplicationException;
}