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
@Table(name="t_doctor_cr_note_details", schema="billing")
@SequenceGenerator(name = "t_doctor_cr_details_seq", sequenceName = "billing.t_doctor_cr_details_seq", allocationSize = 1)
public class DoctorShareCrNoteDetails {

	@Id
	@Column(name="doctor_cr_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_doctor_cr_details_seq")
	private Integer doctorCrDetailsId;
	
	@Column(name = "doctor_cr_id")
	private Integer doctorCrId;
	
	@Column(name = "bill_id")
	private Integer billId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "doctor_credit")
	private Double doctorCredit;
	
	@Column(name = "balance_credit")
	private Double balanceCredit;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "request_status")
	private Integer requestStatus;
	
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
	
	@Column(name = "reject_note")
	private String rejectNote;
	
	@Column(name = "reject_reason_id")
	private Integer rejectReasonId;
	
	@Column(name = "referral_id")
	private Integer referralId;
	
	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Double getBalanceCredit() {
		return balanceCredit;
	}

	public void setBalanceCredit(Double balanceCredit) {
		this.balanceCredit = balanceCredit;
	}

	public Integer getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Integer requestStatus) {
		this.requestStatus = requestStatus;
	}
	
	public Integer getDoctorCrDetailsId() {
		return doctorCrDetailsId;
	}

	public void setDoctorCrDetailsId(Integer doctorCrDetailsId) {
		this.doctorCrDetailsId = doctorCrDetailsId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Double getDoctorCredit() {
		return doctorCredit;
	}

	public void setDoctorCredit(Double doctorCredit) {
		this.doctorCredit = doctorCredit;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
