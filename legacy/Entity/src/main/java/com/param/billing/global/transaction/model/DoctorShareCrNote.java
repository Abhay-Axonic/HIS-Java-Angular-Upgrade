package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_doctor_cr_note", schema="billing")
@SequenceGenerator(name = "t_doctor_cr_seq", sequenceName = "billing.t_doctor_cr_seq", allocationSize = 1)
public class DoctorShareCrNote {

	@Id
	@Column(name="doctor_cr_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_doctor_cr_seq")
	private Integer doctorCrId;
	
	@Column(name = "bill_id")
	private Integer billId;
	
	@Column(name = "total_credit")
	private Double totalCredit;
	
	@Column(name = "unit_id")
	private Integer orgUnitId;
	
	@Column(name = "organization_id")
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
	
	@Column(name = "balance_credit")
	private Double balanceCredit;

	public Double getBalanceCredit() {
		return balanceCredit;
	}

	public void setBalanceCredit(Double balanceCredit) {
		this.balanceCredit = balanceCredit;
	}

	public Integer getDoctorCrId() {
		return doctorCrId;
	}

	public void setDoctorCrId(Integer doctorCrId) {
		this.doctorCrId = doctorCrId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Double getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(Double totalCredit) {
		this.totalCredit = totalCredit;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
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
	
}
