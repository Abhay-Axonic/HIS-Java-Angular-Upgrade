package com.param.billing.global.transaction.dto;

public class TDocSharePaymentVoucherLoggerDto {
	
	private Integer voucherLoggerId;
	private String voucherNo;
	private double processedShareAmt;
	private double retainerShipAmt;
	private double paidAmt;
	private Integer orgUnitId;
	private Integer orgId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Integer doctorId;
	private Double consumedDoctorCr;
	private Double consumedDoctorAdvance;
	private Integer doctorAdvanceId;
	private Integer paymentVoucherId;
	private Integer docShareVoucherDtlsId;
	private Integer docShareVoucherId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getDoctorAdvanceId() {
		return doctorAdvanceId;
	}

	public void setDoctorAdvanceId(Integer doctorAdvanceId) {
		this.doctorAdvanceId = doctorAdvanceId;
	}

	public Double getConsumedDoctorCr() {
		return consumedDoctorCr;
	}

	public void setConsumedDoctorCr(Double consumedDoctorCr) {
		this.consumedDoctorCr = consumedDoctorCr;
	}

	public Double getConsumedDoctorAdvance() {
		return consumedDoctorAdvance;
	}

	public void setConsumedDoctorAdvance(Double consumedDoctorAdvance) {
		this.consumedDoctorAdvance = consumedDoctorAdvance;
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

	

	
}
