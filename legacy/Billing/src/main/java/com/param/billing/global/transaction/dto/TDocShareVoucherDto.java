package com.param.billing.global.transaction.dto;

import java.util.Date;

public class TDocShareVoucherDto {
	private Integer docShareVoucheId;
	private String voucherNo;
	private Double totalDocShareAmout;
	private Integer orgId;
	private Integer orgUnitId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Long updatedDate;
	private Integer updatedBy;
	private Integer doctorShareId;
/*	private Integer docSpecId;*/
	private Integer docId;
	private Double processedShareAmt;
	private Double dueShareAmt;
	private Integer referralId;
	private Double referralSharePer;
	private String remark;
	private Integer type;
	private Date dueDate;
	
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Integer getDocShareVoucheId() {
		return docShareVoucheId;
	}

	public void setDocShareVoucheId(Integer docShareVoucheId) {
		this.docShareVoucheId = docShareVoucheId;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Double getTotalDocShareAmout() {
		return totalDocShareAmout;
	}

	public void setTotalDocShareAmout(Double totalDocShareAmout) {
		this.totalDocShareAmout = totalDocShareAmout;
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

	public Double getProcessedShareAmt() {
		return processedShareAmt;
	}

	public void setProcessedShareAmt(Double processedShareAmt) {
		this.processedShareAmt = processedShareAmt;
	}

	public Double getDueShareAmt() {
		return dueShareAmt;
	}

	public void setDueShareAmt(Double dueShareAmt) {
		this.dueShareAmt = dueShareAmt;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getDoctorShareId() {
		return doctorShareId;
	}

	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}


	public Double getReferralSharePer() {
		return referralSharePer;
	}

	public void setReferralSharePer(Double referralSharePer) {
		this.referralSharePer = referralSharePer;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	
}
