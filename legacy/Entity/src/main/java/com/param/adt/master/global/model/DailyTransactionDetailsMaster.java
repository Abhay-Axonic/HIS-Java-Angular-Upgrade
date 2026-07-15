package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_daily_transaction_details", schema = "public")
@SequenceGenerator(name = "t_daily_transaction_details_id_seq", sequenceName = "public.t_daily_transaction_details_id_seq", allocationSize = 1)

public class DailyTransactionDetailsMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_daily_transaction_details_id_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "is_cash")
	private Boolean isCash; 
	
	@Column(name = "is_card")
	private Boolean isCard; 
	
	@Column(name = "is_cheque")
	private Boolean isCheque; 
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	
	@Column(name = "cash_transaction_details_id")
	private Integer cashTransactionDetailsId;
	
	@Column(name = "cheque_transaction_details_id")
	private Integer chequeTransactionDetailsId;
	
	@Column(name = "card_transaction_details_id")
	private Integer cardTransactionDetailsId;
	
	@Column(name = "cash_refund_id")
	private Integer cashRefundId;
	
	@Column(name = "patient_id")
	private Integer patientId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getIsCash() {
		return isCash;
	}

	public void setIsCash(Boolean isCash) {
		this.isCash = isCash;
	}

	public Boolean getIsCard() {
		return isCard;
	}

	public void setIsCard(Boolean isCard) {
		this.isCard = isCard;
	}

	public Boolean getIsCheque() {
		return isCheque;
	}

	public void setIsCheque(Boolean isCheque) {
		this.isCheque = isCheque;
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

	public Integer getCashTransactionDetailsId() {
		return cashTransactionDetailsId;
	}

	public void setCashTransactionDetailsId(Integer cashTransactionDetailsId) {
		this.cashTransactionDetailsId = cashTransactionDetailsId;
	}

	public Integer getChequeTransactionDetailsId() {
		return chequeTransactionDetailsId;
	}

	public void setChequeTransactionDetailsId(Integer chequeTransactionDetailsId) {
		this.chequeTransactionDetailsId = chequeTransactionDetailsId;
	}

	public Integer getCardTransactionDetailsId() {
		return cardTransactionDetailsId;
	}

	public void setCardTransactionDetailsId(Integer cardTransactionDetailsId) {
		this.cardTransactionDetailsId = cardTransactionDetailsId;
	}

	public Integer getCashRefundId() {
		return cashRefundId;
	}

	public void setCashRefundId(Integer cashRefundId) {
		this.cashRefundId = cashRefundId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	
	
}
