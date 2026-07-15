package com.param.billing.unit.service;

import java.util.List;

import com.param.billing.global.transaction.dto.CancelOrderedServicesDto;
import com.param.billing.unit.dto.TPeriodicityServiceOrdersDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ITPeriodicityServiceOrdersService {
	public Response orderTPeriodicityServices(List<TPeriodicityServiceOrdersDto> listPserv)throws BillingException;
	public Response getTPeriodicityServicesByAdmissionId(Integer admissionId)throws BillingException;
	TPeriodicityServiceOrdersDto placeOrder(TPeriodicityServiceOrdersDto dto)throws BillingException;
	public Response cancelOrderedPeriodicityService(CancelOrderedServicesDto cancelOrderedServicesDto);
	TPeriodicityServiceOrdersDto getPeriodicityServiceDetails(Integer orderDetailsId) throws BillingException;
}
