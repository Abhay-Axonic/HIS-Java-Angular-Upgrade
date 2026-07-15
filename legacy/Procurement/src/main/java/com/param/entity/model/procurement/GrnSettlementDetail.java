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
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "GrnSettlementDetail")
@Table(name = "t_grn_settlement_receipt_detail", schema = "procurement")
public class GrnSettlementDetail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_settlement_id")
	private GrnSettlement grnSettlement;
	@Column(name = "payment_mode_id")
	private Integer paymentModeId;
	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}



	@Column(name = "amount")
	private Double amount;
	@Column(name = "grn_id")
	private Integer grnId;
	@Column(name = "transaction_type_id")
	private Integer transactionTypeId;
	@Column(name = "trans_transaction_date")
	private Date transTransactionDate;

	@Column(name = "cheque_date")
	private Date chequeDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public GrnSettlement getGrnSettlement() {
		return grnSettlement;
	}

	public void setGrnSettlement(GrnSettlement grnSettlement) {
		this.grnSettlement = grnSettlement;
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

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(Integer transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public Date getTransTransactionDate() {
		return transTransactionDate;
	}

	public void setTransTransactionDate(Date transTransactionDate) {
		this.transTransactionDate = transTransactionDate;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	

	@Column(name = "bank_id")
	private Integer bankId;
	@Column(name = "transaction_no")
	private String transactionNo;
	@Column(name = "cheque_number")
	private String chequeNumber;

	@Column(name = "status_id")
	private Integer statusId;

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	

}
