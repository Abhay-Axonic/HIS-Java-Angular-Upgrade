package com.param.global.service;

import com.param.global.common.Response;
import com.param.global.dto.OrderMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IItrimBillService {

	Response saveIntrimBill(OrderMasterDto orderMasterDto) throws ApplicationException;
	
}
