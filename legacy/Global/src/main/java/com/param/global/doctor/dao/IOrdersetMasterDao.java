package com.param.global.doctor.dao;

import com.param.doctor.model.OrdersetMaster;
import com.param.global.common.Response;
import com.param.global.doctor.dto.OrderSetReqDto;
import com.param.global.doctor.dto.OrdersetMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IOrdersetMasterDao extends IGenericDao<OrdersetMaster, Integer>{
	public Response saveOrderset(OrdersetMasterDto dto)throws ApplicationException;

	public Response getOrderSet(OrderSetReqDto reqDto)throws ApplicationException;
	public Response getOrderSetOrders(OrderSetReqDto reqDto)throws ApplicationException;
	
	public Response getInvestigationByOrderSet(OrdersetMasterDto reqDto)throws ApplicationException;
	public Response getMedicationByOrderset(OrdersetMasterDto reqDto)throws ApplicationException;
	
	public Response getOrderSetsList(OrdersetMasterDto dto)throws ApplicationException;
	public Response getOrderSetsListCount(OrdersetMasterDto dto)throws ApplicationException;
	public Response orderSetsDetails(OrdersetMasterDto dto)throws ApplicationException;
	public Response changeStatusOrderSet(Integer ordersetMasterId,Character status)throws ApplicationException;
	public Response orderSetSearch(int unitId,String searchkeyWord,int orderSetActiveTab)throws ApplicationException;
	public Response updateOrderSetsDetails(OrdersetMasterDto dto)throws ApplicationException;

	public Response checkOrderset(OrdersetMasterDto dto)throws ApplicationException;
}
