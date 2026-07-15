package com.param.global.billing.dto;

import java.util.List;

public class PaymentRecieptMasterDto {

	private Integer paymentRecieptId;
	
	private Integer orgnizationId;
	
	private Integer unitId;
	
	private Integer billingMasterId;
	  
	private Double amount;
	
	private Double dummyAmount;
	
	private Double payeeDueAmount;
	
	private String recieptNumber;
	
	private String dateOfReciept;
	
	private Integer receiptType;
	 
	private Integer counterId;
	
	private Integer settlementId;
	
	private Integer payeeId;
	
	private String receivedFrom;
	
	private String identificationNumber;
	
	private String remark;
	
	private Character isPrinted;
	
	private Character isFinancialClearance;
	
	private Character status;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Character isCancelled;
	
	private Integer cancelledBy;
	
	private Integer cancelledReasonId;
	
	private String cancelRemarks;
	
	private String patientName;
	
	private String uhidNumber;
	
	private String bankName;
	
	private Integer bankId;
	
	private String chequeNumber;
	
	private String chequeDate;
	
	private String cardNumber;
	
	private String cardName;
	
	private String transactionNo;
	
	private Integer paymentRecieptDetailsId;
	
	private String currentDate;
	
	private Character isTransferToActiveDeposit;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private Integer visitTypeId;
	
	private Integer patientId;
	
	private String identificationNo;
	
	private List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList;
	
	private Integer depositeConsumptionId;
	
	private String binaryValueDataForSing;
	
	private String signetureImage;
	
	private byte[] signature;

	private Character againstDisallowedAmount;
	
	private Integer  companyInvoiceId;
	
	public Integer getCompanyInvoiceId() {
		return companyInvoiceId;
	}

	public void setCompanyInvoiceId(Integer companyInvoiceId) {
		this.companyInvoiceId = companyInvoiceId;
	}

	public Character getAgainstDisallowedAmount() {
		return againstDisallowedAmount;
	}

	public void setAgainstDisallowedAmount(Character againstDisallowedAmount) {
		this.againstDisallowedAmount = againstDisallowedAmount;
	}

	public Integer getDepositeConsumptionId() {
		return depositeConsumptionId;
	}

	public void setDepositeConsumptionId(Integer depositeConsumptionId) {
		this.depositeConsumptionId = depositeConsumptionId;
	}
	
	public Double getDummyAmount() {
		return dummyAmount;
	}

	public void setDummyAmount(Double dummyAmount) {
		this.dummyAmount = dummyAmount;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public Double getPayeeDueAmount() {
		return payeeDueAmount;
	}

	public void setPayeeDueAmount(Double payeeDueAmount) {
		this.payeeDueAmount = payeeDueAmount;
	}

	public Character getIsTransferToActiveDeposit() {
		return isTransferToActiveDeposit;
	}

	public void setIsTransferToActiveDeposit(Character isTransferToActiveDeposit) {
		this.isTransferToActiveDeposit = isTransferToActiveDeposit;
	}

	public Character getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Character isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Integer getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(Integer cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public Integer getCancelledReasonId() {
		return cancelledReasonId;
	}

	public void setCancelledReasonId(Integer cancelledReasonId) {
		this.cancelledReasonId = cancelledReasonId;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public Integer getPaymentRecieptId() {
		return paymentRecieptId;
	}

	public void setPaymentRecieptId(Integer paymentRecieptId) {
		this.paymentRecieptId = paymentRecieptId;
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

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRecieptNumber() {
		return recieptNumber;
	}

	public void setRecieptNumber(String recieptNumber) {
		this.recieptNumber = recieptNumber;
	}

	public String getDateOfReciept() {
		return dateOfReciept;
	}

	public void setDateOfReciept(String dateOfReciept) {
		this.dateOfReciept = dateOfReciept;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Integer settlementId) {
		this.settlementId = settlementId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getIsPrinted() {
		return isPrinted;
	}

	public void setIsPrinted(Character isPrinted) {
		this.isPrinted = isPrinted;
	}

	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public List<PaymentReceiptDetailsDto> getPaymentReceiptDetailsDtosList() {
		return paymentReceiptDetailsDtosList;
	}

	public void setPaymentReceiptDetailsDtosList(List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList) {
		this.paymentReceiptDetailsDtosList = paymentReceiptDetailsDtosList;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Integer getPaymentRecieptDetailsId() {
		return paymentRecieptDetailsId;
	}

	public void setPaymentRecieptDetailsId(Integer paymentRecieptDetailsId) {
		this.paymentRecieptDetailsId = paymentRecieptDetailsId;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getBinaryValueDataForSing() {
		return binaryValueDataForSing;
	}

	public void setBinaryValueDataForSing(String binaryValueDataForSing) {
		this.binaryValueDataForSing = binaryValueDataForSing;
	}

	public String getSignetureImage() {
		return signetureImage;
	}

	public void setSignetureImage(String signetureImage) {
		this.signetureImage = signetureImage;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	
	

}
