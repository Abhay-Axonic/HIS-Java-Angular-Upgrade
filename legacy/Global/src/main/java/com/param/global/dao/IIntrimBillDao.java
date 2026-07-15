package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IIntrimBillDao {

	Response saveIntrimBill(OrderMasterDto orderMasterDto) throws ApplicationException;
	
	Response saveIntrimBillDetails(OrderDetailsMasterDto orderDetailsMasterDto) throws ApplicationException;

	Response inActivePreviosRecords(OrderMasterDto orderMasterDto);

}
