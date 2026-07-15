package com.param.billing.global.transaction.dao;

import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.BillPayeeDetailsResDto;
import com.param.global.common.Response;
import com.param.global.dto.BillPayeeDetailsDto;
import com.param.billing.global.transaction.model.BillPayeeDetails;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBillPayeeDetailsDao extends IGenericDao<BillPayeeDetails, Integer>{
	public Response saveBillPayeeDetails(BillPayeeDetailsDto billPayeeDetailsDto)throws ApplicationException;
	public Response getBillPayeeDetailsByBillId(int billId)throws BillingException;
	public Response updateDueAmtByPayeeAndBillId(int billId, int payeeId, double dueAmount, double paidAmount)throws BillingException;
	public Response updateCrAmtByPayeeAndBillId(int billId, int payeeId, double crAmount)throws BillingException;
	public Response updatePharamcyBillPayeeDueAmountForCompanyInvoice(int saleId, int payeeId,int associateCompanyId, double amount,double disallowedAmount,double otherDeduction)throws BillingException;//Ganesh
	Response updateBillPayeeDueAmountForCompanyInvoice(int billId, int payeeId,int associateCompanyId, double amount, double disallowedAmount,double otherDeduction)throws BillingException;
	Response updatePayeeDetailsByPayeeAndBillId(BillPayeeDetailsResDto billPayeeDetailsResDto) throws BillingException;
	Response getBillPayeeObjByBillId(int billId) throws BillingException;
	public Response updatePayeeDetailsObject(BillPayeeDetails billPayeeDetails);
	public Response getCreditApproveStatus(Integer billingDetailsId) throws BillingException;
	public Response getCreditPayeeDueAmount(Integer billId, Integer payeeId)throws BillingException;
	public Double getSelfDueAmount(Integer saleId, Integer payeeId);
}

