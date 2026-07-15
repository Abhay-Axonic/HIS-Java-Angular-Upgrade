package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_denomination_details", schema = "public")
@SequenceGenerator(name = "t_denomination_details_id_seq", sequenceName = "public.t_denomination_details_id_seq", allocationSize = 1)
public class DenominationDetailsMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_denomination_details_id_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "denomination")
	private Integer denomination;
	
	@Column(name="quantity")
	private Integer quantity;

	@Column(name="amount")
	private Double amount;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;

	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private String status;
	
	@Column(name="cash_handover_transaction_id")
	private Integer cashHandoverTransactionId;
	
	@Column(name="interim_handover_transaction_id")
	private Integer interimHandoverTransactionId;
	
	@Column(name="cash_in_out_id")
	private Integer cashInoutId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCashHandoverTransactionId() {
		return cashHandoverTransactionId;
	}

	public void setCashHandoverTransactionId(Integer cashHandoverTransactionId) {
		this.cashHandoverTransactionId = cashHandoverTransactionId;
	}

	public Integer getInterimHandoverTransactionId() {
		return interimHandoverTransactionId;
	}

	public void setInterimHandoverTransactionId(Integer interimHandoverTransactionId) {
		this.interimHandoverTransactionId = interimHandoverTransactionId;
	}

	public Integer getDenomination() {
		return denomination;
	}

	public void setDenomination(Integer denomination) {
		this.denomination = denomination;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCashInoutId() {
		return cashInoutId;
	}

	public void setCashInoutId(Integer cashInoutId) {
		this.cashInoutId = cashInoutId;
	}

	
}
