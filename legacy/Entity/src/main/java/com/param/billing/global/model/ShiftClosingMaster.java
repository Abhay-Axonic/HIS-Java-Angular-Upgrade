package com.param.billing.global.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Embeddable
@Table(name = "m_shift_close_balance", schema = "billing")
@SequenceGenerator(name = "shift_close_balance_id_seq", sequenceName = "billing.m_shift_close_balance_id_seq", allocationSize = 1)
public class ShiftClosingMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shift_close_balance_id_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "created_date")
	private Date createDate;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "ich_amount")
	private Double ichAmount;
	
	@Column(name = "to_be_handover")
	private Double toBeHandover;
	
	@Column(name = "mode_of_payment")
	private String modeOfPayment;
	
	@Column(name = "actual_amount")
	private Double actualAmount;
	
	@Column(name = "difference")
	private Double difference;
	
	@Column(name = "refund_amount")
	private Double refundAmount;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Double getIchAmount() {
		return ichAmount;
	}

	public void setIchAmount(Double ichAmount) {
		this.ichAmount = ichAmount;
	}

	public Double getToBeHandover() {
		return toBeHandover;
	}

	public void setToBeHandover(Double toBeHandover) {
		this.toBeHandover = toBeHandover;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	
	
	
}
