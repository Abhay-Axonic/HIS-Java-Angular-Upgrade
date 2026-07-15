package com.param.service.dto;


import java.util.Date;
import java.util.List;

public class FundInFlowDetailsDto {
	
	private Integer fundInFlowDetailsId;
	  
	private Integer fundInFlowId;
	
	private Integer trustId;
	
	private Integer paymentModeId;
	  
	private Double amount;
	 
	private Integer bankId;
	
	private String bankName;
	
	private String trustName;
	
	private String accountNumber;
	  
	private String chequeNumber;
	  
	private String cardNumber;
	  
	private String cardHolderName;
	  
	private String dateOfTransaction;
	  
	private String expiryDate;
	
	private String toDate;
	
	private String fromDate;
	  
	private Integer unitId;

	private Integer orgId;

	private Character status;

	private String createdDate;
	
	private Date creditDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private Date chequeDate;
	
	private Character isCancle;
	
	private Integer cardTypeId;
	
	private String cardNo; 
	
	private Integer transactionTypeId;
	
	private String transactionNo;
	
	private String transTransactionDate;
	
	private String interimCardStatus;
	
	private String interimChequeStatus;
	
	private String cashOpeingDate;
	
	private Date currentDate;
	
	private String cardName;
	
	private String patientName;
	
	private String recieptNumber;
	
	private String uhidNumber;
	
	private Double availableFund;
	
	private Double totalCollectedAmount;
	
	private Double loyaltyCardAmt;
	
	private Double cardBalAmount;
	
	private Double cashAmount;
	
	private Double chequeAmount;
	
	private Double finalAmount;
	
	private Double cardAmount;
	
	private Double transferAmount;
	
	private Integer otp;
	
	private String transactionType;
	
	private List<FundInFlowDetailsDto> fundInFlowDetailsDto;
	
	private List<Integer> fundInFlowDiagnosisDetails;
	
	private List<FundSanctionAcceptanceRequestDto > fundSanctionAcceptanceRequestDto;
	
	
	private Integer patientId;
	
	private String fundRemark;
	
	private Integer fundtypeId;
	
	private Double availableAmount;
	
	private Double trustAcceptedAmount;
	
	private Double billConsumeAmount;
	
	
	
	
	

	public Integer getFundInFlowDetailsId() {
		return fundInFlowDetailsId;
	}

	public void setFundInFlowDetailsId(Integer fundInFlowDetailsId) {
		this.fundInFlowDetailsId = fundInFlowDetailsId;
	}

	public Integer getFundInFlowId() {
		return fundInFlowId;
	}

	public void setFundInFlowId(Integer fundInFlowId) {
		this.fundInFlowId = fundInFlowId;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
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

	public String getTrustName() {
		return trustName;
	}

	public void setTrustName(String trustName) {
		this.trustName = trustName;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
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

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
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

	public Double getTotalCollectedAmount() {
		return totalCollectedAmount;
	}

	public void setTotalCollectedAmount(Double totalCollectedAmount) {
		this.totalCollectedAmount = totalCollectedAmount;
	}

	public Double getLoyaltyCardAmt() {
		return loyaltyCardAmt;
	}

	public void setLoyaltyCardAmt(Double loyaltyCardAmt) {
		this.loyaltyCardAmt = loyaltyCardAmt;
	}

	public Double getCardBalAmount() {
		return cardBalAmount;
	}

	public void setCardBalAmount(Double cardBalAmount) {
		this.cardBalAmount = cardBalAmount;
	}

	public Double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Double cashAmount) {
		this.cashAmount = cashAmount;
	}

	public Double getChequeAmount() {
		return chequeAmount;
	}

	public void setChequeAmount(Double chequeAmount) {
		this.chequeAmount = chequeAmount;
	}

	public Double getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public List<FundInFlowDetailsDto> getFundInFlowDetailsDto() {
		return fundInFlowDetailsDto;
	}

	public void setFundInFlowDetailsDto(List<FundInFlowDetailsDto> fundInFlowDetailsDto) {
		this.fundInFlowDetailsDto = fundInFlowDetailsDto;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	@Override
	public String toString() {
		return "FundInFlowDetailsDto [fundInFlowDetailsId=" + fundInFlowDetailsId + ", fundInFlowId=" + fundInFlowId
				+ ", trustId=" + trustId + ", paymentModeId=" + paymentModeId + ", amount=" + amount + ", bankId="
				+ bankId + ", bankName=" + bankName + ", trustName=" + trustName + ", accountNumber=" + accountNumber
				+ ", chequeNumber=" + chequeNumber + ", cardNumber=" + cardNumber + ", cardHolderName=" + cardHolderName
				+ ", dateOfTransaction=" + dateOfTransaction + ", expiryDate=" + expiryDate + ", toDate=" + toDate
				+ ", fromDate=" + fromDate + ", unitId=" + unitId + ", orgId=" + orgId + ", status=" + status
				+ ", createdDate=" + createdDate + ", createdBy=" + createdBy + ", updatedDate=" + updatedDate
				+ ", updatedBy=" + updatedBy + ", chequeDate=" + chequeDate + ", isCancle=" + isCancle + ", cardTypeId="
				+ cardTypeId + ", cardNo=" + cardNo + ", transactionTypeId=" + transactionTypeId + ", transactionNo="
				+ transactionNo + ", transTransactionDate=" + transTransactionDate + ", interimCardStatus="
				+ interimCardStatus + ", interimChequeStatus=" + interimChequeStatus + ", cashOpeingDate="
				+ cashOpeingDate + ", currentDate=" + currentDate + ", cardName=" + cardName + ", patientName="
				+ patientName + ", recieptNumber=" + recieptNumber + ", uhidNumber=" + uhidNumber + ", availableFund="
				+ availableFund + ", totalCollectedAmount=" + totalCollectedAmount + ", loyaltyCardAmt="
				+ loyaltyCardAmt + ", cardBalAmount=" + cardBalAmount + ", cashAmount=" + cashAmount + ", chequeAmount="
				+ chequeAmount + ", finalAmount=" + finalAmount + ", otp=" + otp + ", fundInFlowDetailsDto="
				+ fundInFlowDetailsDto + "]";
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Double getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(Double cardAmount) {
		this.cardAmount = cardAmount;
	}

	public Double getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Date getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(Date creditDate) {
		this.creditDate = creditDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getFundRemark() {
		return fundRemark;
	}

	public void setFundRemark(String fundRemark) {
		this.fundRemark = fundRemark;
	}

	public Integer getFundtypeId() {
		return fundtypeId;
	}

	public void setFundtypeId(Integer fundtypeId) {
		this.fundtypeId = fundtypeId;
	}

	public List<Integer> getFundInFlowDiagnosisDetails() {
		return fundInFlowDiagnosisDetails;
	}

	public void setFundInFlowDiagnosisDetails(List<Integer> fundInFlowDiagnosisDetails) {
		this.fundInFlowDiagnosisDetails = fundInFlowDiagnosisDetails;
	}

	public Double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Double availableAmount) {
		this.availableAmount = availableAmount;
	}

	public Double getTrustAcceptedAmount() {
		return trustAcceptedAmount;
	}

	public void setTrustAcceptedAmount(Double trustAcceptedAmount) {
		this.trustAcceptedAmount = trustAcceptedAmount;
	}

	public List<FundSanctionAcceptanceRequestDto> getFundSanctionAcceptanceRequestDto() {
		return fundSanctionAcceptanceRequestDto;
	}

	public void setFundSanctionAcceptanceRequestDto(
			List<FundSanctionAcceptanceRequestDto> fundSanctionAcceptanceRequestDto) {
		this.fundSanctionAcceptanceRequestDto = fundSanctionAcceptanceRequestDto;
	}

	public Double getBillConsumeAmount() {
		return billConsumeAmount;
	}

	public void setBillConsumeAmount(Double billConsumeAmount) {
		this.billConsumeAmount = billConsumeAmount;
	}



	
}
