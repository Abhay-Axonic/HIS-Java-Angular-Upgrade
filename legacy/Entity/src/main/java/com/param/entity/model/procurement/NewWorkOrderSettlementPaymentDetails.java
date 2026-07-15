package com.param.entity.model.procurement;

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
@Table(name = "t_new_work_order_settlement_payment_details", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_settlement_payment_details_seq", sequenceName = "procurement.t_new_work_order_settlement_payment_details_seq", allocationSize = 1)

public class NewWorkOrderSettlementPaymentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_settlement_payment_details_seq")
	@Column(name = "workorder_settlement_payment_details_id")
	private Integer workorderSettlementPaymentDetailsId;

	@Column(name = "workorder_settlement_id")
	private Integer workorderSettlementId;

	@Column(name = "payment_mode_id")
	private Integer paymentModeId;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "bank_id")
	private Integer bankId;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "check_number")
	private String chequeNumber;
	
	@Column(name = "cheque_date")
	private Date chequeDate;
	
	

	@Column(name = "card_type_id")
	private Integer cardTypeId;

	@Column(name = "card_number")
	private String cardNumber;

	@Column(name = "card_holder_name")
	private String cardHolderName;
	
	@Column(name = "expiry_date")
	private Date expiryDate;

	
	
	@Column(name = "transaction_type_id")
	private Integer transactionTypeId;

	@Column(name = "transaction_no")
	private String transactionNo;

	@Column(name = "trans_transaction_date")
	private Date transTransactionDate;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "org_Id")
	private Integer orgId;

	@Column(name = "status")
	private String status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public Integer getWorkorderSettlementPaymentDetailsId() {
		return workorderSettlementPaymentDetailsId;
	}

	public void setWorkorderSettlementPaymentDetailsId(Integer workorderSettlementPaymentDetailsId) {
		this.workorderSettlementPaymentDetailsId = workorderSettlementPaymentDetailsId;
	}

	public Integer getWorkorderSettlementId() {
		return workorderSettlementId;
	}

	public void setWorkorderSettlementId(Integer workorderSettlementId) {
		this.workorderSettlementId = workorderSettlementId;
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

	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
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

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	

}
