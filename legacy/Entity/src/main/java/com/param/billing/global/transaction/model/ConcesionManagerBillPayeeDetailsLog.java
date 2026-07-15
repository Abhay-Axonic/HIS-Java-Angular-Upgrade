package com.param.billing.global.transaction.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.param.global.common.DateConverter;


@Entity
@Table(name="t_concession_manager_bill_payee_details_logs",schema="billing")
public class ConcesionManagerBillPayeeDetailsLog {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="bill_payee_details_id")
	private Integer billPayeeDetailsId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="bill_id")
	private Integer billId;
	
	@Column(name="bill_amount")
	private Double billAmount;
	
	@Column(name="due_amount")
	private Double dueAmount;
	
	@Column(name = "disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;
	
	@Column(name="updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name = "cr_amount")
	private Double crAmount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
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

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Double getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(Double crAmount) {
		this.crAmount = crAmount;
	}
	
}
