package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_company_receipts_expense_reciept_details", schema = "billing")
public class CompanyReceiptsExpenseRecieptDetails {

	@Id
	@Column(name = "company_receipts_expense_reciept_details_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer companyReceiptsExpenseRecieptDetailsId;
	
	@Column(name="company_receipts_expense_reciept_id")
	private Integer companyReceiptsExpenseRecieptId;
	
	@Column(name = "payment_mode_id")
	private Integer paymentModeId;
	  
	@Column(name = "amount")
	private Double amount;
	 
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

	@Column(name = "org_id")
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
	
	@Column(name = "hospital_account_id")
	private Integer hospitalAccountId;
	
	@Column(name="loyalty_card_id")
	private Integer loyaltyCardId;

	public Integer getCompanyReceiptsExpenseRecieptDetailsId() {
		return companyReceiptsExpenseRecieptDetailsId;
	}

	public void setCompanyReceiptsExpenseRecieptDetailsId(Integer companyReceiptsExpenseRecieptDetailsId) {
		this.companyReceiptsExpenseRecieptDetailsId = companyReceiptsExpenseRecieptDetailsId;
	}

	public Integer getCompanyReceiptsExpenseRecieptId() {
		return companyReceiptsExpenseRecieptId;
	}

	public void setCompanyReceiptsExpenseRecieptId(Integer companyReceiptsExpenseRecieptId) {
		this.companyReceiptsExpenseRecieptId = companyReceiptsExpenseRecieptId;
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

	public Integer getHospitalAccountId() {
		return hospitalAccountId;
	}

	public void setHospitalAccountId(Integer hospitalAccountId) {
		this.hospitalAccountId = hospitalAccountId;
	}

	public Integer getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(Integer loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}
	
	
}
