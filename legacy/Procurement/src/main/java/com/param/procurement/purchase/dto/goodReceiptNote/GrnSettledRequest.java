package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.List;

import javax.persistence.Column;

import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto1;

public class GrnSettledRequest {
	protected Integer id;
	protected Integer orgnizationId;
	protected Integer unitId;
	protected Integer userId;
	protected String amountReceivedFrom;
	protected  String identificaitonNumber;
	public String getIdentificaitonNumber() {
		return identificaitonNumber;
	}
	public void setIdentificaitonNumber(String identificaitonNumber) {
		this.identificaitonNumber = identificaitonNumber;
	}
	protected List<CreditNoteAmountList> creditNoteAmountList;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public List<CreditNoteAmountList> getCreditNoteAmountList() {
		return creditNoteAmountList;
	}
	public void setCreditNoteAmountList(List<CreditNoteAmountList> creditNoteAmountList) {
		this.creditNoteAmountList = creditNoteAmountList;
	}
	public String getAmountReceivedFrom() {
		return amountReceivedFrom;
	}
	public void setAmountReceivedFrom(String amountReceivedFrom) {
		this.amountReceivedFrom = amountReceivedFrom;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrgnizationId() {
		return orgnizationId;
	}
	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Double getAmountData() {
		return amountData;
	}
	public void setAmountData(Double amountData) {
		this.amountData = amountData;
	}
	public List<PaymentReceiptDetailsDto1> getPaymentReceiptDetailsDtosList() {
		return PaymentReceiptDetailsDtosList;
	}
	public void setPaymentReceiptDetailsDtosList(List<PaymentReceiptDetailsDto1> paymentReceiptDetailsDtosList) {
		PaymentReceiptDetailsDtosList = paymentReceiptDetailsDtosList;
	}
	protected Double amountData;
	protected Double totalCreditNoteAmtUse;
	protected List<PaymentReceiptDetailsDto1> PaymentReceiptDetailsDtosList;
	public Double getTotalCreditNoteAmtUse() {
		return totalCreditNoteAmtUse;
	}
	public void setTotalCreditNoteAmtUse(Double totalCreditNoteAmtUse) {
		this.totalCreditNoteAmtUse = totalCreditNoteAmtUse;
	}
}
