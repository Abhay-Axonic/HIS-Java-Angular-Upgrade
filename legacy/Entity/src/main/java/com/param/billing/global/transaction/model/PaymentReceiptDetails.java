package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
@NamedNativeQuery(name = "GET_PAYMENT_INVOICES_BY_BILL_ID",
			query="select"
					+ "   prm.reciept_number as \"receiptNo\","
					+ "   to_char(prm.date_of_reciept, 'DD-MM-YYYY') as \"dateOfReceipt\","
					+ "   prm.billing_master_id as \"billId\","
					+ "   prm.amount as \"invoiceAmount\","
					+ "   prm.payment_reciept_id as \"paymentRecieptId\","
					+ "   prm.created_by as \"createdBy\","
					+ "   usr.user_name as \"receivedBy\" "
					+ " from"
					+ "   billing.t_payment_reciept_master prm"
					+ "   left join public.m_user_master usr on usr.user_id = prm.created_by"
					+ " where"
					+ "   prm.billing_master_id =:billId"
					+ "   and prm.orgnization_id =:orgId"
					+ "   and prm.unit_id =:orgUnitId"),//2568

	@NamedNativeQuery(name = "GET_PAYMENT_RECIEPT_DETAILS_FOR_ADDENDUM_BY_BILL_ID",
					 query = "select prd.payment_mode_id as \"paymentModeId\", sum(prd.amount) as amount,null as \"bankId\", null as \"accountNumber\",  null as \"chequeNumber\", "
							 + " null as \"cardNumber\", null as \"cardHolderName\", null as \"dateOfTransaction\", null as \"expiryDate\", null as \"cardTypeId\", null as \"transactionTypeId\", "
							 + " null as \"transactionNo\", null as \"transTransactionDate\" "
							 + " from billing.t_payment_reciept_details prd "
							 + " inner join billing.t_payment_reciept_master prm on prm.payment_reciept_id = prd.payment_receipt_master_id "
							 + " where prm.billing_master_id = :billId and prd.payment_mode_id = 1 "
							 + " group by prd.payment_mode_id "
							 + " union all "
							 + " select prd.payment_mode_id as \"paymentModeId\", prd.amount as \"amount\", prd.bank_id as \"bankId\",  "
							 + " prd.account_number as \"accountNumber\", prd.check_number as \"chequeNumber\", prd.card_number as \"cardNumber\", "
							 + " prd.card_holder_name as \"cardHolderName\", to_char(prd.date_of_transaction,'DD-MM-YYYY HH24:MI:SS') as \"dateOfTransaction\", "
							 + " to_char(prd.expiry_date,'DD-MM-YYYY') as \"expiryDate\", prd.card_type_id as \"cardTypeId\",  "
							 + " prd.transaction_type_id as \"transactionTypeId\", prd.transaction_no as \"transactionNo\",  "
							 + " to_char(prd.trans_transaction_date,'DD-MM-YYYY') as \"transTransactionDate\" "
							 + " from billing.t_payment_reciept_details prd "
							 + " inner join billing.t_payment_reciept_master prm on prm.payment_reciept_id = prd.payment_receipt_master_id "
							 + " where prm.billing_master_id = :billId and prd.payment_mode_id != 1 ")
	
	

})

@Entity
@Table(name="t_payment_reciept_details", schema="billing")
@SequenceGenerator(name="payment_reciept_details_seq", sequenceName="billing.payment_reciept_details_seq", allocationSize=1)
public class PaymentReceiptDetails {
	@Id
	@Column(name = "payment_reciept_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_reciept_details_seq")
	private Integer paymentRecieptDetailsId;
	  
	@Column(name="payment_receipt_master_id")
	private Integer paymentReceiptMasterId;
	
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

	@Column(name = "orgnization_id")
	private Integer orgnizationId;

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
	
	@Column(name = "hospital_account_id")
	private Integer hospitalAccountId;
	
	@Column(name="loyalty_card_id")
	private Integer loyaltyCardId;
	
	@Column(name="is_mpesa_validation")
	private boolean skipMpesaValidation;
	
	@Column(name="device_name")
	private String deviceName;
	
	@Column(name="transaction_details")
	private String transactionDetails;
	
	public Integer getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(Integer loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}

	public Integer getHospitalAccountId() {
		return hospitalAccountId;
	}

	public void setHospitalAccountId(Integer hospitalAccountId) {
		this.hospitalAccountId = hospitalAccountId;
	}

	public Double getAvailableFund() {
		return availableFund;
	}

	public void setAvailableFund(Double availableFund) {
		this.availableFund = availableFund;
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

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
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

	public String getCheckNumber() {
		return chequeNumber;
	}

	public void setCheckNumber(String chequeNumber) {
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

	public Integer getOrgnizationId() {
		return orgnizationId;
	}

	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
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

	public boolean isExportedToShiftClose() {
		return exportedToShiftClose;
	}

	public void setExportedToShiftClose(boolean exportedToShiftClose) {
		this.exportedToShiftClose = exportedToShiftClose;
	}

	public boolean isSkipMpesaValidation() {
		return skipMpesaValidation;
	}

	public void setSkipMpesaValidation(boolean skipMpesaValidation) {
		this.skipMpesaValidation = skipMpesaValidation;
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
	
}
