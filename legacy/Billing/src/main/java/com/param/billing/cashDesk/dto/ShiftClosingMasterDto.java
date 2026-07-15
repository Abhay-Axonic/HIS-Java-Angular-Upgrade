package com.param.billing.cashDesk.dto;

import java.util.List;
public class ShiftClosingMasterDto {
	
	private Integer id;
	
	private String recieptNumber;
	
	private String createdDate;
	
	private String pName;
	
	private String uhidNumber;
	
	private Double amount;
	
	private String bankName;
	
	private String chequeNumber;
	
	private String cardNumber;
	
	private String cardTypeName;
	
	private String transactionNo;

	private String ichNumber;
	
	private Double cashHandoverAmount;
	
	private Integer createdBy;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer cashHandoverTransactionId;
	
	private Double cashHandoverAmountlog;
	
	private Double ichAmount;
	
	private Double toBeHandover;
	
	private String modeOfPayment;
	
	private Double actualAmount;
	
	private Double actualAmountCash;
	
	private Double amountCash;
	
	private String closingNumber;
	
	private Double difference;
	
	private String updatedDate;

	private Integer updatedBy;
	
	private Integer handoverBy;
	
	private Integer handoverTo;
	
	private String cashierName;
	
	private String status;
	
	private String openingDate;

	private String voucherNo;
	
	private Double refundAmount;
	
	private String fromDate;
	
	private String toDate;
	
	private String userName;
	
	private String currentDate;
	
	private Integer paymentReceiptId;
	
	private Integer paymentReceiptDetailsId;
	
 	private Integer paymentRefundId;
	
	private Integer interimHandoverId;
	
	private boolean shiftClose;
	
	private List<BillingIdDetailsDto> listOfIds;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRecieptNumber() {
		return recieptNumber;
	}

	public void setRecieptNumber(String recieptNumber) {
		this.recieptNumber = recieptNumber;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getIchNumber() {
		return ichNumber;
	}

	public void setIchNumber(String ichNumber) {
		this.ichNumber = ichNumber;
	}

	public Double getCashHandoverAmountlog() {
		return cashHandoverAmountlog;
	}

	public void setCashHandoverAmountlog(Double cashHandoverAmountlog) {
		this.cashHandoverAmountlog = cashHandoverAmountlog;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getCashHandoverTransactionId() {
		return cashHandoverTransactionId;
	}

	public void setCashHandoverTransactionId(Integer cashHandoverTransactionId) {
		this.cashHandoverTransactionId = cashHandoverTransactionId;
	}

	public Double getIchAmount() {
		return ichAmount;
	}

	public void setIchAmount(Double ichAmount) {
		this.ichAmount = ichAmount;
	}

	public Double getToBeHandover() {
		return toBeHandover;
	}

	public void setToBeHandover(Double toBeHandover) {
		this.toBeHandover = toBeHandover;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Double getActualAmountCash() {
		return actualAmountCash;
	}

	public void setActualAmountCash(Double actualAmountCash) {
		this.actualAmountCash = actualAmountCash;
	}

	public Double getAmountCash() {
		return amountCash;
	}

	public void setAmountCash(Double amountCash) {
		this.amountCash = amountCash;
	}

	public String getClosingNumber() {
		return closingNumber;
	}

	public void setClosingNumber(String closingNumber) {
		this.closingNumber = closingNumber;
	}

	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getHandoverBy() {
		return handoverBy;
	}

	public void setHandoverBy(Integer handoverBy) {
		this.handoverBy = handoverBy;
	}

	public Integer getHandoverTo() {
		return handoverTo;
	}

	public void setHandoverTo(Integer handoverTo) {
		this.handoverTo = handoverTo;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Double getCashHandoverAmount() {
		return cashHandoverAmount;
	}

	public void setCashHandoverAmount(Double cashHandoverAmount) {
		this.cashHandoverAmount = cashHandoverAmount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public List<BillingIdDetailsDto> getListOfIds() {
		return listOfIds;
	}

	public void setListOfIds(List<BillingIdDetailsDto> listOfIds) {
		this.listOfIds = listOfIds;
	}

	public Integer getPaymentReceiptId() {
		return paymentReceiptId;
	}

	public void setPaymentReceiptId(Integer paymentReceiptId) {
		this.paymentReceiptId = paymentReceiptId;
	}

	public Integer getPaymentRefundId() {
		return paymentRefundId;
	}

	public void setPaymentRefundId(Integer paymentRefundId) {
		this.paymentRefundId = paymentRefundId;
	}

	public Integer getInterimHandoverId() {
		return interimHandoverId;
	}

	public void setInterimHandoverId(Integer interimHandoverId) {
		this.interimHandoverId = interimHandoverId;
	}

	public Integer getPaymentReceiptDetailsId() {
		return paymentReceiptDetailsId;
	}

	public void setPaymentReceiptDetailsId(Integer paymentReceiptDetailsId) {
		this.paymentReceiptDetailsId = paymentReceiptDetailsId;
	}

	public boolean isShiftClose() {
		return shiftClose;
	}

	public void setShiftClose(boolean shiftClose) {
		this.shiftClose = shiftClose;
	}

	
}
