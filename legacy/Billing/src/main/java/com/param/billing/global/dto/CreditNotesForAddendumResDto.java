package com.param.billing.global.dto;

public class CreditNotesForAddendumResDto {
	private String creditNoteNo;
	private String creditNoteDate;
	private Integer creditNoteId;
	private Integer billId;
	private Double creditNoteAmount;
	private Double amountPaid;
	public String getCreditNoteNo() {
		return creditNoteNo;
	}
	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}
	public String getCreditNoteDate() {
		return creditNoteDate;
	}
	public void setCreditNoteDate(String creditNoteDate) {
		this.creditNoteDate = creditNoteDate;
	}
	public Integer getCreditNoteId() {
		return creditNoteId;
	}
	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public Double getCreditNoteAmount() {
		return creditNoteAmount;
	}
	public void setCreditNoteAmount(Double creditNoteAmount) {
		this.creditNoteAmount = creditNoteAmount;
	}
	public Double getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}
}
