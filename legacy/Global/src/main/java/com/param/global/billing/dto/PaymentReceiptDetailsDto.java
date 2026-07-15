package com.param.global.billing.dto;

public class PaymentReceiptDetailsDto {
	
	private Integer paymentRecieptDetailsId;
	  
	private Integer paymentReceiptMasterId;
	
	private Integer paymentModeId;
	
	private Integer trustId;
	  
	private Double amount;
	 
	private Integer bankId;
	
	private String bankName;
	  
	private String accountNumber;
	  
	private String chequeNumber;
	  
	private String cardNumber;
	  
	private String cardHolderName;
	  
	private String dateOfTransaction;
	  
	private String expiryDate;
	  
	private Integer unitId;

	private Integer orgnizationId;

	private Character status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private String chequeDate;
	
	private String transactionType;
	
	private Character isCancle;
	
	private Integer cardTypeId;
	
	private String cardNo; 
	
	private Integer transactionTypeId;
	
	private String transactionNo;
	
	private String transTransactionDate;
	
	private String interimCardStatus;
	
	private String interimChequeStatus;
	
	private String cashOpeingDate;
	
	private String currentDate;
	
	private String cardName;
	
	private String patientName;
	
	private String recieptNumber;
	
	private String uhidNumber;
	
	private Double availableFund;
	
	private String binaryValueDataForSing;
	private String RequestedUniqueId;

	//loyaltyCard changes
	private double loyaltyCardAmt;
	
	private double cardBalAmount;
	
	private Integer otp;
	
	private Integer hospitalAccountId;
	
	private Integer loyaltyCardId;
	
	private Boolean skipMpesaValidation;
	
	private Integer paymentGatewayProviderId;
	
	private String deviceName;
	private String transactionDetails;
	private String receivedFrom;
	public Integer getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(Integer loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}

	public Integer getPaymentRecieptDetailsId() {
		return paymentRecieptDetailsId;
	}

	public void setPaymentRecieptDetailsId(Integer paymentRecieptDetailsId) {
		this.paymentRecieptDetailsId = paymentRecieptDetailsId;
	}

	public Integer getPaymentReceiptMasterId() {
		return paymentReceiptMasterId;
	}

	public void setPaymentReceiptMasterId(Integer paymentReceiptMasterId) {
		this.paymentReceiptMasterId = paymentReceiptMasterId;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getDateOfTransaction() {
		return dateOfTransaction;
	}

	public void setDateOfTransaction(String dateOfTransaction) {
		this.dateOfTransaction = dateOfTransaction;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public String getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public Character getIsCancle() {
		return isCancle;
	}

	public void setIsCancle(Character isCancle) {
		this.isCancle = isCancle;
	}

	public Integer getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(Integer cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(Integer transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getTransTransactionDate() {
		return transTransactionDate;
	}

	public void setTransTransactionDate(String transTransactionDate) {
		this.transTransactionDate = transTransactionDate;
	}

	public String getInterimCardStatus() {
		return interimCardStatus;
	}

	public void setInterimCardStatus(String interimCardStatus) {
		this.interimCardStatus = interimCardStatus;
	}

	public String getInterimChequeStatus() {
		return interimChequeStatus;
	}

	public void setInterimChequeStatus(String interimChequeStatus) {
		this.interimChequeStatus = interimChequeStatus;
	}

	public String getCashOpeingDate() {
		return cashOpeingDate;
	}

	public void setCashOpeingDate(String cashOpeingDate) {
		this.cashOpeingDate = cashOpeingDate;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getRecieptNumber() {
		return recieptNumber;
	}

	public void setRecieptNumber(String recieptNumber) {
		this.recieptNumber = recieptNumber;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Double getAvailableFund() {
		return availableFund;
	}

	public void setAvailableFund(Double availableFund) {
		this.availableFund = availableFund;
	}

	public double getLoyaltyCardAmt() {
		return loyaltyCardAmt;
	}

	public void setLoyaltyCardAmt(double loyaltyCardAmt) {
		this.loyaltyCardAmt = loyaltyCardAmt;
	}

	public double getCardBalAmount() {
		return cardBalAmount;
	}

	public void setCardBalAmount(double cardBalAmount) {
		this.cardBalAmount = cardBalAmount;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public Integer getHospitalAccountId() {
		return hospitalAccountId;
	}

	public void setHospitalAccountId(Integer hospitalAccountId) {
		this.hospitalAccountId = hospitalAccountId;
	}

	public Integer getOrgnizationId() {
		return orgnizationId;
	}

	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}

	public String getBinaryValueDataForSing() {
		return binaryValueDataForSing;
	}

	public void setBinaryValueDataForSing(String binaryValueDataForSing) {
		this.binaryValueDataForSing = binaryValueDataForSing;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Boolean getSkipMpesaValidation() {
		return skipMpesaValidation;
	}

	public void setSkipMpesaValidation(Boolean skipMpesaValidation) {
		this.skipMpesaValidation = skipMpesaValidation;
	}

	public Integer getPaymentGatewayProviderId() {
		return paymentGatewayProviderId;
	}

	public void setPaymentGatewayProviderId(Integer paymentGatewayProviderId) {
		this.paymentGatewayProviderId = paymentGatewayProviderId;
	}

	public String getRequestedUniqueId() {
		return RequestedUniqueId;
	}

	public void setRequestedUniqueId(String requestedUniqueId) {
		RequestedUniqueId = requestedUniqueId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
	
	
	
}
