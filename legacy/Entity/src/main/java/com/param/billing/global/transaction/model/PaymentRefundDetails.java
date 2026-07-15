package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.PaymentModeMaster;
import com.param.billing.global.model.BankMaster;
import com.param.billing.global.model.CardTypeMaster;

@NamedQueries({
@NamedQuery(name = "GET_REFUND_DETAILS_BY_REFUND_ID", query = "SELECT paymentModeMst.paymentModeDesc AS paymentMode, "
		+ "	bankMst.bankName AS bankName, " 
		+ "	paymentRefundDetails.chequeNumber AS chequeNumber , "
		+ "	paymentRefundDetails.cardNo AS cardNo, "
		+ "	paymentRefundDetails.amount AS amount "
		+ "	FROM PaymentRefundDetails paymentRefundDetails "
		+ "	INNER JOIN paymentRefundDetails.paymentModeMaster paymentModeMst "
		+ "	LEFT JOIN paymentRefundDetails.bankMaster bankMst "
		+ "	WHERE paymentRefundDetails.paymentRefundId = :paymentRefundId"
		+ "	AND paymentRefundDetails.status = 'A' ")

})
@Entity
@Table(name = "t_payment_refund_details", schema = "billing")
@SequenceGenerator(name = "refund_voucher_payment_mode_details_seq", sequenceName = "billing.refund_voucher_payment_mode_details_seq", allocationSize = 1)
public class PaymentRefundDetails {
	@Id
	@Column(name = "payment_refund_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refund_voucher_payment_mode_details_seq")
	private int paymentRefundDetailsId;

	@Column(name = "payment_refund_id")
	private Integer paymentRefundId;

	@Column(name = "payment_mode_id")
	private Integer paymentModeId;

	@Column(name = "amount")
	private double amount;

	@Column(name = "bank_id")
	private Integer bankId;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "card_number")
	private String cardNo;

	@Column(name = "cheque_number")
	private String chequeNumber;

	@Column(name = "card_holder_name")
	private String cardHolderName;

	@Column(name = "date_of_refund")
	private Date dateOfRefund;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "orgnisation_id")
	private Integer orgnisationId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "hospital_account_id")
	private Integer hospitalAccountId;
	
	@Column(name = "transaction_type_id")
	private Integer transactionTypeId;
	
	@Column(name = "transaction_no")
	private String transactionNo;
	
	@Column(name = "trans_transaction_date")
	private Date transTransactionDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_mode_id", insertable = false, updatable = false, nullable = false)
	private PaymentModeMaster paymentModeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_id", insertable = false, updatable = false, nullable = false)
	private BankMaster bankMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_refund_id", insertable = false, updatable = false, nullable = false)
	private PaymentRefundMaster paymentRefundMaster;


	public int getPaymentRefundDetailsId() {
		return paymentRefundDetailsId;
	}

	public void setPaymentRefundDetailsId(int paymentRefundDetailsId) {
		this.paymentRefundDetailsId = paymentRefundDetailsId;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public Date getDateOfRefund() {
		return dateOfRefund;
	}

	public void setDateOfRefund(Date dateOfRefund) {
		this.dateOfRefund = dateOfRefund;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public Integer getPaymentRefundId() {
		return paymentRefundId;
	}

	public void setPaymentRefundId(Integer paymentRefundId) {
		this.paymentRefundId = paymentRefundId;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public PaymentModeMaster getPaymentModeMaster() {
		return paymentModeMaster;
	}

	public void setPaymentModeMaster(PaymentModeMaster paymentModeMaster) {
		this.paymentModeMaster = paymentModeMaster;
	}

	public BankMaster getBankMaster() {
		return bankMaster;
	}

	public void setBankMaster(BankMaster bankMaster) {
		this.bankMaster = bankMaster;
	}

	public PaymentRefundMaster getPaymentRefundMaster() {
		return paymentRefundMaster;
	}

	public void setPaymentRefundMaster(PaymentRefundMaster paymentRefundMaster) {
		this.paymentRefundMaster = paymentRefundMaster;
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

	public Date getTransTransactionDate() {
		return transTransactionDate;
	}

	public void setTransTransactionDate(Date transTransactionDate) {
		this.transTransactionDate = transTransactionDate;
	}

}
