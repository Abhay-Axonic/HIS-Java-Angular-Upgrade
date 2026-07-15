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
@Table(name = "m_concession_manager_bill_log", schema = "billing")
public class ConcessionManagerBillLog {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "billing_master_id")
	private Integer billingMasterId;

	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "visit_type_id")
	private Integer visitTypeId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "bill_number")
	private String billNumber;

	@Column(name = "bill_date_time")
	@Convert(converter = DateConverter.class)
	private Long billDateTime;

	@Column(name = "net_amount")
	private Double netAmount;

	@Column(name = "discount_amount")
	private Double discountAmount;

	@Column(name = "concession_amount")
	private Double concessionAmount;

	@Column(name = "gst")
	private Double gst;

	@Column(name = "total_bill_amount")
	private Double totalBillAmount;

	@Column(name = "outstanding")
	private Double outstanding;

	@Column(name = "round_off")
	private Double roundOff;

	@Column(name = "is_credit_bill")
	private Character isCreditBill;

	@Column(name = "is_invoice_creation")
	private Character isInvoiceCreation;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "bill_status_id")
	private Integer billStatusId;

	@Column(name = "cr_amount")
	private Double crAmount;

	@Column(name = "settlement_date")
	@Convert(converter = DateConverter.class)
	private Long settlementDate;

	@Column(name = "bill_cancelled")
	private Character billCancelled;
	
	@Column(name = "is_addendum_bill")
	private Character isAddendumBill;
	
	@Column(name = "old_bill_id")
	private Integer oldBillId;
	
	@Column(name = "is_doctor_referral_posted")
	private Character isDoctorReferralPosted;

	@Column(name = "bill_cancel_reason_id")
	private Integer billCancelReasonId;

	@Column(name = "bill_cancel_datetime")
	@Convert(converter = DateConverter.class)
	private Long billCancelDatetime;

	@Column(name = "bill_cancel_by")
	private Integer billCancelBy;

	@Column(name = "bill_cancel_remark")
	private String billCancelRemark;

	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "is_interim_or_final_bill")
	private Integer isInterimOrFinalBill;

	@Column(name = "is_dr_note")
	private Character isDrNote;

	@Column(name = "dr_note_against_billid")
	private Integer drNoteAgainstBillId;
	
	@Column(name="counter_id")
	private Integer counterId;
	
	@Column(name="is_intermediate_bill")
	private Character isIntermediateBill;
	
	@Column(name = "total_self_payable")
	private Double selfPayable;
	
	@Column(name = "total_credit_payable")
	private Double creditPayable;
	
	@Column(name = "disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name="disallowed_against_invoice_dtl_id")
	private Integer disallowedAgainstInvoiceDtlsId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Long getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(Long billDateTime) {
		this.billDateTime = billDateTime;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getConcessionAmount() {
		return concessionAmount;
	}

	public void setConcessionAmount(Double concessionAmount) {
		this.concessionAmount = concessionAmount;
	}

	public Double getGst() {
		return gst;
	}

	public void setGst(Double gst) {
		this.gst = gst;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public Character getIsCreditBill() {
		return isCreditBill;
	}

	public void setIsCreditBill(Character isCreditBill) {
		this.isCreditBill = isCreditBill;
	}

	public Character getIsInvoiceCreation() {
		return isInvoiceCreation;
	}

	public void setIsInvoiceCreation(Character isInvoiceCreation) {
		this.isInvoiceCreation = isInvoiceCreation;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public Double getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(Double crAmount) {
		this.crAmount = crAmount;
	}

	public Long getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Long settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Character getBillCancelled() {
		return billCancelled;
	}

	public void setBillCancelled(Character billCancelled) {
		this.billCancelled = billCancelled;
	}

	public Character getIsAddendumBill() {
		return isAddendumBill;
	}

	public void setIsAddendumBill(Character isAddendumBill) {
		this.isAddendumBill = isAddendumBill;
	}

	public Integer getOldBillId() {
		return oldBillId;
	}

	public void setOldBillId(Integer oldBillId) {
		this.oldBillId = oldBillId;
	}

	public Character getIsDoctorReferralPosted() {
		return isDoctorReferralPosted;
	}

	public void setIsDoctorReferralPosted(Character isDoctorReferralPosted) {
		this.isDoctorReferralPosted = isDoctorReferralPosted;
	}

	public Integer getBillCancelReasonId() {
		return billCancelReasonId;
	}

	public void setBillCancelReasonId(Integer billCancelReasonId) {
		this.billCancelReasonId = billCancelReasonId;
	}

	public Long getBillCancelDatetime() {
		return billCancelDatetime;
	}

	public void setBillCancelDatetime(Long billCancelDatetime) {
		this.billCancelDatetime = billCancelDatetime;
	}

	public Integer getBillCancelBy() {
		return billCancelBy;
	}

	public void setBillCancelBy(Integer billCancelBy) {
		this.billCancelBy = billCancelBy;
	}

	public String getBillCancelRemark() {
		return billCancelRemark;
	}

	public void setBillCancelRemark(String billCancelRemark) {
		this.billCancelRemark = billCancelRemark;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getIsInterimOrFinalBill() {
		return isInterimOrFinalBill;
	}

	public void setIsInterimOrFinalBill(Integer isInterimOrFinalBill) {
		this.isInterimOrFinalBill = isInterimOrFinalBill;
	}

	public Character getIsDrNote() {
		return isDrNote;
	}

	public void setIsDrNote(Character isDrNote) {
		this.isDrNote = isDrNote;
	}

	public Integer getDrNoteAgainstBillId() {
		return drNoteAgainstBillId;
	}

	public void setDrNoteAgainstBillId(Integer drNoteAgainstBillId) {
		this.drNoteAgainstBillId = drNoteAgainstBillId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Character getIsIntermediateBill() {
		return isIntermediateBill;
	}

	public void setIsIntermediateBill(Character isIntermediateBill) {
		this.isIntermediateBill = isIntermediateBill;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public Integer getDisallowedAgainstInvoiceDtlsId() {
		return disallowedAgainstInvoiceDtlsId;
	}

	public void setDisallowedAgainstInvoiceDtlsId(Integer disallowedAgainstInvoiceDtlsId) {
		this.disallowedAgainstInvoiceDtlsId = disallowedAgainstInvoiceDtlsId;
	}
	
	
}