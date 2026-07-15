package com.param.billing.global.transaction.service;

import java.util.List;

import com.param.billing.common.Response;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.UpdatePayeeDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBillingDetailsService {
	public Response saveBillingDetails(List<BillingDetailsDto> billingDetailsDtoList)throws ApplicationException;
	public Response getBillDetailsByBillId(int orgId,int unitId,int billId)throws ApplicationException;
	public Response getTransactionLogs(BillingDetailsDto billingDetailsDto)throws ApplicationException;
	public Response getBillServicesForAddendumBill(int billId)throws ApplicationException;
	public Response updatePayeeForPackage(UpdatePayeeDto billingMasterDto, List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList, List<OrderDetailsByEncounterIdDto> packageList, List<OrderDetailsMasterDto> list) throws Exception;
	
}
