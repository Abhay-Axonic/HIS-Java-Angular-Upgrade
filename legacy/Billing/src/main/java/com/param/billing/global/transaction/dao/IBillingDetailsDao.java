package com.param.billing.global.transaction.dao;

import java.util.List;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.model.BillingDetails;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBillingDetailsDao extends IGenericDao<BillingDetails, Integer>{
	public Response saveBillingDetails(List<BillingDetailsDto> billingDetailsDtoList)throws ApplicationException;
	public Response getBillDetailsByBillId(int orgId,int unitId,int billId)throws ApplicationException;
	public Response getBillingDetailsByBillId(BillingDetailsDto billingDetailsDto)throws ApplicationException;
	public Response getTransactionLogs(BillingDetailsDto billingDetailsDto)throws ApplicationException;
	public Response getBillServicesForAddendumBill(int billId)throws ApplicationException;
	List<BillingDetailsDto> getBillingDetailsByCreditNoteId(int creditNoteId) throws ApplicationException;
	public Response canceBillDetailsAndUpdateCrDetails(Integer billDetalilsId, Integer creditNoteId)throws ApplicationException;
	Response getBillingDetailsByBillIdForInvoiceCreation(Integer billId,Integer payeeId) throws ApplicationException;
	Response updateBillDetailsIsDoctorSharePostedByBillId(List<Integer> billDtlsIds) throws ServiceException;
	Response getBillDetailsObjByBillId(Integer billId) throws ApplicationException;
	Response updateBillDetailsObj(BillingDetails billingDetails) throws ApplicationException;
	
}
