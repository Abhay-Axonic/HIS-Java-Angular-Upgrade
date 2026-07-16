package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.billing.global.model.BankMaster;
import com.param.billing.global.model.CardTypeMaster;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.PaymentMode;

@Entity(name="SalePaymentMode")
@Table(name = "t_sale_payment_mode", schema = "pharmacy")
public class SalePaymentMode extends BaseEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;
	
	@Column(name="check_number")
	private String checkNumber;
	
	@Column(name="card_holder_name")
	private String cardHolderName;
	
	@Column(name="account_number")
	private String accountNumber;
	
	@Column(name="card_number")
	private String cardNumber;
	
	@Column(name="cheque_date")
	private Timestamp chequeDate;
	
	@Column(name="expiry_date")
	private Timestamp expiryDate;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="transaction_no")
	private String transactionNo;
	
	@Column(name="transaction_type_id ")
	private Integer transactionTypeId ;
	
	@Column(name="trans_transaction_date ")
	private Timestamp transTransactionDate ;
	
    @Column(name="is_mpesa_validation")
    private boolean skipMpesaValidation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_id")
	Sale sale;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_mode_id")
	PaymentMode paymentMode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_id")
	BankMaster bankMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "card_type_id")
	CardTypeMaster cardTypeMaster;

	@Column(name="device_name")
	private String deviceName;
	
	@Column(name="transaction_details")
	private String transactionDetails;
	
	public SalePaymentMode() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public String getTransactionNo() {
		return transactionNo;
	}



	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Timestamp getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Timestamp chequeDate) {
		this.chequeDate = chequeDate;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public BankMaster getBankMaster() {
		return bankMaster;
	}

	public void setBankMaster(BankMaster bankMaster) {
		this.bankMaster = bankMaster;
	}

	public CardTypeMaster getCardTypeMaster() {
		return cardTypeMaster;
	}

	public void setCardTypeMaster(CardTypeMaster cardTypeMaster) {
		this.cardTypeMaster = cardTypeMaster;
	}



	public Integer getTransactionTypeId() {
		return transactionTypeId;
	}



	public void setTransactionTypeId(Integer transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}



	public Timestamp getTransTransactionDate() {
		return transTransactionDate;
	}



	public void setTransTransactionDate(Timestamp transTransactionDate) {
		this.transTransactionDate = transTransactionDate;
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
