package com.param.billing.unit.dao;

import java.util.Date;

import com.param.billing.global.model.TPeriodicityServiceOrders;
import com.param.billing.unit.dto.TPeriodicityServiceOrdersDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITPeriodicityServiceOrdersDao extends IGenericDao<TPeriodicityServiceOrders, Integer>{
	public Response saveTPeriodicityServiceOrders(TPeriodicityServiceOrdersDto dto)throws BillingException;
	public Response getTPeriodicityServiceOrderForBatchProcessing(Date toDate, int startPoint, int maxResults)throws BillingException;
	public Response updateTPeriodicityNoOfUnitsById(int noOfUnits,int id, int orderStatusId)throws BillingException;
	public Response getTPeriodicityServicesByAdmissionId(Integer admissionId)throws BillingException;
	public Response getAdmissionByIdForBatchPServicePosting(int admissionId)throws ApplicationException;
	public Response cancelOrderedPeriodicityService(Integer periodicityServiceOrderId, Character character)throws ApplicationException;
	TPeriodicityServiceOrdersDto getTPeriodicityServicesByOrderDetailsId(Integer orderDetailsId) throws BillingException;
}
