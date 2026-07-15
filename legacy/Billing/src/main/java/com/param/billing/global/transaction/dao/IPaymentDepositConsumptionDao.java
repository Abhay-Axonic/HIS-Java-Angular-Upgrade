package com.param.billing.global.transaction.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.param.billing.dto.CompanyReceiptMasterDto;
import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.model.PaymentDepositConsumption;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementReqDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings("rawtypes")
public interface IPaymentDepositConsumptionDao extends IGenericDao<PaymentDepositConsumption,Integer>{

	Response addDepositConsumption(BillPaymentCollectionReqDto reqDto) throws ApplicationException;

	Response addDepositConsumptionDetails(PaymentDepositDetailsDto paymentDepositDetailsDto)
			throws ApplicationException;

	Long getNextValDepositeConsumptionId()throws ApplicationException;

	List<BillPaymentCollectionReqDto> getPaymentRecieptDetails(Integer billId) throws ApplicationException;

	Response addDepositAfterCancelReciept(Integer depositConsumptionId);

	Response getBillPharmacyPayeeByBillIdCreditNote(int saleId) throws ApplicationException;

	Response cancelPharmacyReceipt(BillPaymentCollectionReqDto dto)throws ApplicationException;

	Response checkInvoiceSettleOrNotByBillId(CompanyInvoiceSettlementReqDto reqDto)throws ApplicationException;

}
