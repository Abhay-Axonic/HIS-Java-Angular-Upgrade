package com.param.billing.global.transaction.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@Entity
@Table(name="t_doc_share_payment_voucher", schema="billing")
@SequenceGenerator(name = "t_seq_doc_share_payment_voucher", sequenceName = "billing.t_seq_doc_share_payment_voucher", allocationSize = 1)
public class TDocSharePaymentVoucher {

	@Id
	@Column(name="payment_voucher_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_doc_share_payment_voucher")
	private Integer paymentVoucherId;
	
	@Column(name = "payment_voucher_no")
	private String paymentVoucherNo;
	
	@Column(name = "total_processed_amt")
	private Double totalProcessedAmt;
	
	@Column(name = "total_retainer_ship_amt")
	private Double totalRetainerShipAmt;
	
	@Column(name = "total_paid_amt")
	private Double totalPaidAmt;
	
	@Column(name = "total_consumed_doctor_cr")
	private Double totalConsumedDoctorCr;
	
	@Column(name = "total_consumed_doctor_advance")
	private Double totalConsumedDoctorAdvance;

	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "referral_id")
	private Integer referralId;
	
	@Column(name = "tds_amt")
	private Double tdsAmt;
	
	
	public Double getTdsAmt() {
		return tdsAmt;
	}

	public void setTdsAmt(Double tdsAmt) {
		this.tdsAmt = tdsAmt;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPaymentVoucherId() {
		return paymentVoucherId;
	}

	public void setPaymentVoucherId(Integer paymentVoucherId) {
		this.paymentVoucherId = paymentVoucherId;
	}

	public String getPaymentVoucherNo() {
		return paymentVoucherNo;
	}

	public void setPaymentVoucherNo(String paymentVoucherNo) {
		this.paymentVoucherNo = paymentVoucherNo;
	}

	public Double getTotalProcessedAmt() {
		return totalProcessedAmt;
	}

	public void setTotalProcessedAmt(Double totalProcessedAmt) {
		this.totalProcessedAmt = totalProcessedAmt;
	}

	public Double getTotalRetainerShipAmt() {
		return totalRetainerShipAmt;
	}

	public void setTotalRetainerShipAmt(Double totalRetainerShipAmt) {
		this.totalRetainerShipAmt = totalRetainerShipAmt;
	}

	public Double getTotalPaidAmt() {
		return totalPaidAmt;
	}

	public void setTotalPaidAmt(Double totalPaidAmt) {
		this.totalPaidAmt = totalPaidAmt;
	}

	public Double getTotalConsumedDoctorCr() {
		return totalConsumedDoctorCr;
	}

	public void setTotalConsumedDoctorCr(Double totalConsumedDoctorCr) {
		this.totalConsumedDoctorCr = totalConsumedDoctorCr;
	}

	public Double getTotalConsumedDoctorAdvance() {
		return totalConsumedDoctorAdvance;
	}

	public void setTotalConsumedDoctorAdvance(Double totalConsumedDoctorAdvance) {
		this.totalConsumedDoctorAdvance = totalConsumedDoctorAdvance;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
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
	
}
