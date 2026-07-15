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
@Table(name="t_doc_share_payment_voucher_logger", schema="billing")
@SequenceGenerator(name = "t_seq_doc_share_payment_voucher_logger", sequenceName = "billing.t_seq_doc_share_payment_voucher_logger", allocationSize = 1)
public class TDocSharePaymentVoucherLogger {
	@Id
	@Column(name="voucher_logger_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_doc_share_payment_voucher_logger")
	private Integer voucherLoggerId;
	
	@Column(name = "payment_voucher_id")
	private Integer paymentVoucherId;
	
	@Column(name = "voucher_no")
	private String voucherNo;
	
	@Column(name = "processed_amt")
	private double processedShareAmt;
	
	@Column(name = "retainer_ship_amt")
	private double retainerShipAmt;
	
	@Column(name = "paid_amt")
	private double paidAmt;
	
	@Column(name = "consumed_doctor_cr")
	private Double consumedDoctorCr;
	
	@Column(name = "consumed_doctor_advance")
	private Double consumedDoctorAdvance;

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
	
	@Column(name = "doc_share_voucher_dtls_id")
	private Integer docShareVoucherDtlsId;
	
	@Column(name = "doc_share_voucher_id")
	private Integer docShareVoucherId;
	
	@Column(name = "tds_amt")
	private double tdsAmt;
	
	
	
	public double getTdsAmt() {
		return tdsAmt;
	}

	public void setTdsAmt(double tdsAmt) {
		this.tdsAmt = tdsAmt;
	}

	public Integer getDocShareVoucherId() {
		return docShareVoucherId;
	}

	public void setDocShareVoucherId(Integer docShareVoucherId) {
		this.docShareVoucherId = docShareVoucherId;
	}

	public Integer getDocShareVoucherDtlsId() {
		return docShareVoucherDtlsId;
	}

	public void setDocShareVoucherDtlsId(Integer docShareVoucherDtlsId) {
		this.docShareVoucherDtlsId = docShareVoucherDtlsId;
	}

	public Integer getPaymentVoucherId() {
		return paymentVoucherId;
	}

	public void setPaymentVoucherId(Integer paymentVoucherId) {
		this.paymentVoucherId = paymentVoucherId;
	}

	public Double getConsumedDoctorAdvance() {
		return consumedDoctorAdvance;
	}

	public void setConsumedDoctorAdvance(Double consumedDoctorAdvance) {
		this.consumedDoctorAdvance = consumedDoctorAdvance;
	}

	public Double getConsumedDoctorCr() {
		return consumedDoctorCr;
	}

	public void setConsumedDoctorCr(Double consumedDoctorCr) {
		this.consumedDoctorCr = consumedDoctorCr;
	}

	public Integer getVoucherLoggerId() {
		return voucherLoggerId;
	}

	public void setVoucherLoggerId(Integer voucherLoggerId) {
		this.voucherLoggerId = voucherLoggerId;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public double getProcessedShareAmt() {
		return processedShareAmt;
	}

	public void setProcessedShareAmt(double processedShareAmt) {
		this.processedShareAmt = processedShareAmt;
	}

	public double getRetainerShipAmt() {
		return retainerShipAmt;
	}

	public void setRetainerShipAmt(double retainerShipAmt) {
		this.retainerShipAmt = retainerShipAmt;
	}

	public double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(double paidAmt) {
		this.paidAmt = paidAmt;
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
