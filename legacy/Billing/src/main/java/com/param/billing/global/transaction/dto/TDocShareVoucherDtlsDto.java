package com.param.billing.global.transaction.dto;




public class TDocShareVoucherDtlsDto {
	
	private Integer voucherDtlsId;
	private Integer docShareVoucheId;
	private Integer doctorShareId;
	private double processedAmt;
	private Integer orgUnitId;
	private Integer orgId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Long updatedDate;
	private Integer updatedBy;
	private Integer  referralShareId;
	private Integer doctorSlabTransactionId;
	
	public Integer getDoctorSlabTransactionId() {
		return doctorSlabTransactionId;
	}

	public void setDoctorSlabTransactionId(Integer doctorSlabTransactionId) {
		this.doctorSlabTransactionId = doctorSlabTransactionId;
	}

	public Integer getVoucherDtlsId() {
		return voucherDtlsId;
	}

	public void setVoucherDtlsId(Integer voucherDtlsId) {
		this.voucherDtlsId = voucherDtlsId;
	}

	public Integer getDocShareVoucheId() {
		return docShareVoucheId;
	}

	public void setDocShareVoucheId(Integer docShareVoucheId) {
		this.docShareVoucheId = docShareVoucheId;
	}

	public Integer getDoctorShareId() {
		return doctorShareId;
	}

	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}

	public double getProcessedAmt() {
		return processedAmt;
	}

	public void setProcessedAmt(double processedAmt) {
		this.processedAmt = processedAmt;
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

	public Integer getReferralShareId() {
		return referralShareId;
	}

	public void setReferralShareId(Integer referralShareId) {
		this.referralShareId = referralShareId;
	}

	
	
	
}
