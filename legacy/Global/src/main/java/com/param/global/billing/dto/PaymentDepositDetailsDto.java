package com.param.global.billing.dto;

import com.param.global.service.OrderMasterServiceImpl;

public class PaymentDepositDetailsDto {
	private Integer depositDetailsId;
	private Integer depConsumptionId;
	private Integer depositId;
	private Integer paymentModeId;
	private Double amount;
	private Integer bankId;
	private String accountNumber;
	private String chequeNumber;
	private String cardNo;
	private String cardHolderName;
	private String expiryDate;
	private Integer cardTypeId;
	private String chequeDate;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private String bankName;
	private Double consumedAmount;
	private Double balanceAmount;
	private Double refundAmount;
	private Integer organizationId;
	private Integer unitId;
	private Integer paymentRecieptDetailsId;
	private Double depositConsumed;
	private Integer hospitalAccountId;
	private Integer transactionTypeId;
	private String transactionNo;
	private String transTransactionDate;
	private String transactionType;
	private Integer companyReceiptsExpenseId;
	private Boolean skipMpesaValidation;
	private Integer paymentGatewayProviderId;
	private String requestedUniqueId;
	private String deviceName;
	private String transactionTypeName;
	private String creditDebitCardType;
	private String transactionDetails;
	private String remark;
	private Integer paymentRecieptId;

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}
	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
	}
	public Double getDepositConsumed() {
		return depositConsumed;
	}
	public void setDepositConsumed(Double depositConsumed) {
		this.depositConsumed = depositConsumed;
	}
	public Integer getPaymentRecieptDetailsId() {
		return paymentRecieptDetailsId;
	}
	public void setPaymentRecieptDetailsId(Integer paymentRecieptDetailsId) {
		this.paymentRecieptDetailsId = paymentRecieptDetailsId;
	}
	public Integer getDepConsumptionId() {
		return depConsumptionId;
	}
	public void setDepConsumptionId(Integer depConsumptionId) {
		this.depConsumptionId = depConsumptionId;
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
	public Integer getDepositDetailsId() {
		return depositDetailsId;
	}
	public void setDepositDetailsId(Integer depositDetailsId) {
		this.depositDetailsId = depositDetailsId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = new Double(OrderMasterServiceImpl.df2.format(amount));
	}
	public Double getConsumedAmount() {
		return consumedAmount;
	}
	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = new Double(OrderMasterServiceImpl.df2.format(consumedAmount));
	}
	public Double getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = new Double(OrderMasterServiceImpl.df2.format(balanceAmount));
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getDepositId() {
		return depositId;
	}
	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}
	public Integer getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
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
	public Double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public Integer getHospitalAccountId() {
		return hospitalAccountId;
	}
	public void setHospitalAccountId(Integer hospitalAccountId) {
		this.hospitalAccountId = hospitalAccountId;
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
		return requestedUniqueId;
	}
	public void setRequestedUniqueId(String requestedUniqueId) {
		this.requestedUniqueId = requestedUniqueId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getTransactionTypeName() {
		return transactionTypeName;
	}
	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}
	public String getCreditDebitCardType() {
		return creditDebitCardType;
	}
	public void setCreditDebitCardType(String creditDebitCardType) {
		this.creditDebitCardType = creditDebitCardType;
	}
	public String getTransactionDetails() {
		return transactionDetails;
	}
	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}
	public Integer getPaymentRecieptId() {
		return paymentRecieptId;
	}
	public void setPaymentRecieptId(Integer paymentRecieptId) {
		this.paymentRecieptId = paymentRecieptId;
	}
	
	
}
