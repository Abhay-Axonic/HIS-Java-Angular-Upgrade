package com.param.pharmacy.op.dto.billList;

public class PaymentReceiptDetailsDtosList {

	protected Integer paymentModeId;
	
	protected Double amount;
	
	protected Integer createdBy;
	
	protected String cardHolderName;
	
	protected Integer cardTypeId;
	
	protected String cardNumber;
	
	protected String cardNo;
	
	protected String expiryDate;
	
	private String chequeDate;
	
	Integer bankId;
	
	Integer cardTypeMasterId;
	
	private String chequeNumber;
	
	private String accountNumber;
	
	private String transactionNo;
	
	private Integer transactionTypeId;

	private String transTransactionDate;

	private String transactionType;
	
	private String bankName;
	private Boolean skipMpesaValidation;
	
	private Integer paymentGatewayProviderId;
	
	private String deviceName;
	private String transactionDetails;
    private String receivedFrom;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCardTypeMasterId() {
		return cardTypeMasterId;
	}

	public void setCardTypeMasterId(Integer cardTypeMasterId) {
		this.cardTypeMasterId = cardTypeMasterId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public Integer getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(Integer cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/*
	 * public String getCardNo() { return cardNo; }
	 * 
	 * public void setCardNo(String cardNo) { this.cardNo = cardNo; }
	 */

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
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

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTransTransactionDate() {
		return transTransactionDate;
	}

	public void setTransTransactionDate(String transTransactionDate) {
		this.transTransactionDate = transTransactionDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
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