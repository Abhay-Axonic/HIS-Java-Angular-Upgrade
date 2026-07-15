package com.param.billing.global.transaction.dto;



public class TDoctorShareSplitLogsDto {
	
	private Integer doctorShareSplitId;
	private Integer doctorShareId;
	private Integer docId;
	private double oldSharePercentage;
	private double newSharePercentage;
	private Integer orgUnitId;
	private Integer orgId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Long updatedDate;
	private Integer updatedBy;
	private double shareAmount;
	private Integer priority;
	private Integer parentDocShareId;

	public Integer getParentDocShareId() {
		return parentDocShareId;
	}

	public void setParentDocShareId(Integer parentDocShareId) {
		this.parentDocShareId = parentDocShareId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public double getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(double shareAmount) {
		this.shareAmount = shareAmount;
	}

	public Integer getDoctorShareSplitId() {
		return doctorShareSplitId;
	}

	public void setDoctorShareSplitId(Integer doctorShareSplitId) {
		this.doctorShareSplitId = doctorShareSplitId;
	}

	public Integer getDoctorShareId() {
		return doctorShareId;
	}

	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}

	

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public double getOldSharePercentage() {
		return oldSharePercentage;
	}

	public void setOldSharePercentage(double oldSharePercentage) {
		this.oldSharePercentage = oldSharePercentage;
	}

	public double getNewSharePercentage() {
		return newSharePercentage;
	}

	public void setNewSharePercentage(double newSharePercentage) {
		this.newSharePercentage = newSharePercentage;
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
	
	

}
