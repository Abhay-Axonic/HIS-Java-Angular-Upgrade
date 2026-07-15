package com.param.global.billing.dto;


import java.math.BigDecimal;

public class FranchiseDepositDetailsDto {

    private int frDepositDetailId;

    private Integer frDepositId;

    private Integer paymentModeId;

    private double amount;

    private Integer bankId;

    private String accountNumber;

    private String chequeNumber;

    private String cardNo;

    private String bankName;

    private BigDecimal totalAmount;

    private String cardHolderName;

    private String expiryDate;

    private Integer cardTypeId;

    private String chequeDate;

    private Double consumedAmount;

    private Double balanceAmount;

    private double refundAmount;

    private String createdDate;

    private Integer createdBy;

    private String updatedDate;

    private Integer updatedBy;

    private Integer hospitalAccountId;

    private Integer paymentRecieptDetailsId;

    private Integer transactionTypeId;

    private String transactionNo;

    private String transTransactionDate;

    private Boolean skipMpesaValidation;

    private String deviceName;

    private Integer paymentGatewayProviderId;

    private String requestedUniqueId;

    private Integer unitId;

    private String paymentMode;

    private String transactionType;
    private String transactionDetails;

    public int getFrDepositDetailId() {
        return frDepositDetailId;
    }

    public void setFrDepositDetailId(int frDepositDetailId) {
        this.frDepositDetailId = frDepositDetailId;
    }

    public Integer getFrDepositId() {
        return frDepositId;
    }

    public void setFrDepositId(Integer frDepositId) {
        this.frDepositId = frDepositId;
    }

    public Integer getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(Integer paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public Double getConsumedAmount() {
        return consumedAmount;
    }

    public void setConsumedAmount(Double consumedAmount) {
        this.consumedAmount = consumedAmount;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
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

    public Integer getHospitalAccountId() {
        return hospitalAccountId;
    }

    public void setHospitalAccountId(Integer hospitalAccountId) {
        this.hospitalAccountId = hospitalAccountId;
    }

    public Integer getPaymentRecieptDetailsId() {
        return paymentRecieptDetailsId;
    }

    public void setPaymentRecieptDetailsId(Integer paymentRecieptDetailsId) {
        this.paymentRecieptDetailsId = paymentRecieptDetailsId;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getPaymentGatewayProviderId() {
        return paymentGatewayProviderId;
    }

    public void setPaymentGatewayProviderId(Integer paymentGatewayProviderId) {
        this.paymentGatewayProviderId = paymentGatewayProviderId;
    }

    public Boolean getSkipMpesaValidation() {
        return skipMpesaValidation;
    }

    public void setSkipMpesaValidation(Boolean skipMpesaValidation) {
        this.skipMpesaValidation = skipMpesaValidation;
    }

    public String getRequestedUniqueId() {
        return requestedUniqueId;
    }

    public void setRequestedUniqueId(String requestedUniqueId) {
        this.requestedUniqueId = requestedUniqueId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

	public String getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}
    
    
}
