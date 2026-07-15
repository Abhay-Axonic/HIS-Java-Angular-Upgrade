package com.param.billing.global.transaction.dto;

import java.util.List;
import java.util.Map;

import com.param.billing.dto.CompanyReceiptMasterDto;
import com.param.billing.global.dto.BillingPaymentDetailsReqDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.global.billing.dto.PaymentDepositDetailsList;

public class CompanyInvoiceSettlementReqDto {
	private CompanyReceiptMasterDto companyReceipt;
	private Map<Integer,Double> companyRecptRecAmount;
	private List<BillingPaymentDetailsReqDto> paymentDetails;
	private List<TCompanyInvoiceServiceDetailsDto> recvAndDisallAmountDetails;
	private Integer selfPayeeId;
	private List<PaymentDepositDetailsList> paymentDepositDetailsList;
	
	

	public List<PaymentDepositDetailsList> getPaymentDepositDetailsList() {
		return paymentDepositDetailsList;
	}

	public void setPaymentDepositDetailsList(List<PaymentDepositDetailsList> paymentDepositDetailsList) {
		this.paymentDepositDetailsList = paymentDepositDetailsList;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public List<BillingPaymentDetailsReqDto> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<BillingPaymentDetailsReqDto> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public Map<Integer, Double> getCompanyRecptRecAmount() {
		return companyRecptRecAmount;
	}

	public void setCompanyRecptRecAmount(Map<Integer, Double> companyRecptRecAmount) {
		this.companyRecptRecAmount = companyRecptRecAmount;
	}

	public CompanyReceiptMasterDto getCompanyReceipt() {
		return companyReceipt;
	}

	public void setCompanyReceipt(CompanyReceiptMasterDto companyReceipt) {
		this.companyReceipt = companyReceipt;
	}

	public List<TCompanyInvoiceServiceDetailsDto> getRecvAndDisallAmountDetails() {
		return recvAndDisallAmountDetails;
	}

	public void setRecvAndDisallAmountDetails(
			List<TCompanyInvoiceServiceDetailsDto> recvAndDisallAmountDetails) {
		this.recvAndDisallAmountDetails = recvAndDisallAmountDetails;
	}
	
	
	
}