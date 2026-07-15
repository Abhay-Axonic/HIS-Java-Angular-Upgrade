package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity 
@Table(name="m_fund_inflow_details", schema="billing")
@SequenceGenerator(name="m_fund_inflow_details_seq", sequenceName="billing.m_fund_inflow_details_seq", allocationSize=1)
public class FundInFlowDetails {
	@Id
	@Column(name = "fund_inflow_detail_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_fund_inflow_details_seq")
	private Integer fundInFlowDetailsId;
	  
	@Column(name="fund_inflow_id")
	private Integer fundInFlowId;
	
	@Column(name = "payment_mode_id")
	private Integer paymentModeId;
	  
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "cash_amount")
	private Double cashAmount; 
	
	@Column(name = "cheque_amount")
	private Double chequeAmount;
	  
	 
	@Column(name = "bank_id")
	private Integer bankId;
	  
	@Column(name = "account_number")
	private String accountNumber;
	  
	@Column(name = "check_number")
	private String  chequeNumber;
	  
	@Column(name = "card_number")
	private String cardNumber;
	  
	@Column(name = "card_holder_name")
	private String cardHolderName;
	  
	@Column(name = "date_of_transaction")
	private Date dateOfTransaction;
	  
	@Column(name = "expiry_date")
	private Date expiryDate;
	  
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "org_Id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name="cheque_date")
	private Date chequeDate;
	
	@Column(name="is_cancle")
	private Character isCancle;
	
	@Column(name="card_type_id")
	private Integer cardTypeId;
	
	@Column(name = "transaction_type_id")
	private Integer transactionTypeId;
	
	@Column(name = "transaction_no")
	private String transactionNo;
	
	@Column(name = "trans_transaction_date")
	private Date transTransactionDate;

	@Column(name="interim_card_status")
	private String interimCardStatus;
	
	@Column(name="interim_cheque_status")
	private String interimChequeStatus;
	
	@Column(name="available_fund")
	private Double availableFund;
	
	@Column(name="exported_to_shift_close")
	private boolean exportedToShiftClose;

	@Column(name="transfer_amount")
	private Double transferAmount;
	
	@Column(name="card_amount")
	private Double cardAmount;
	
	/*@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="bank_id", insertable=false, updatable=false)
	private BankMaster bankMaster;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="fund_inflow_id", insertable=false, updatable=false)
	private FundInFlowMaster FundInFlowMaster;
	*/
	
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

	public Date getDateOfTransaction() {
		return dateOfTransaction;
	}

	public void setDateOfTransaction(Date dateOfTransaction) {
		this.dateOfTransaction = dateOfTransaction;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
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

	public Date getTransTransactionDate() {
		return transTransactionDate;
	}

	public void setTransTransactionDate(Date transTransactionDate) {
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

	public Double getAvailableFund() {
		return availableFund;
	}

	public void setAvailableFund(Double availableFund) {
		this.availableFund = availableFund;
	}

	public boolean isExportedToShiftClose() {
		return exportedToShiftClose;
	}

	public void setExportedToShiftClose(boolean exportedToShiftClose) {
		this.exportedToShiftClose = exportedToShiftClose;
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

	public Double getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Double getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(Double cardAmount) {
		this.cardAmount = cardAmount;
	}

/*	public BankMaster getBankMaster() {
		return bankMaster;
	}

	public void setBankMaster(BankMaster bankMaster) {
		this.bankMaster = bankMaster;
	}

	public FundInFlowMaster getFundInFlowMaster() {
		return FundInFlowMaster;
	}

	public void setFundInFlowMaster(FundInFlowMaster fundInFlowMaster) {
		FundInFlowMaster = fundInFlowMaster;
	}
*/

}
