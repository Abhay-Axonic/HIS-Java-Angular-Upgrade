package com.param.billing.global.transaction.dto;

import java.util.Date;

public class DoctorAdvanceDto {

	private Integer doctorAdvanceId;
	
	private Double totalAdvance;
	
	private Double balanceAdvance;
	
	private Integer orgUnitId;
	
	private Integer orgId;

	private Integer requestStatus;
	
	private Character status;
	
	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private Integer doctorId;
	
	private String doctorName;
	
	private Integer doctorAdvanceDetailsId;
	
	private Double requestedAdvance;
	
	private Double approvedAdvance;
	
	private String fromDate;
	
	private String toDate;
	
	private String remark;
	
	private Integer specialityId;
	
	private String specialityName;
	
	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getDoctorAdvanceDetailsId() {
		return doctorAdvanceDetailsId;
	}

	public void setDoctorAdvanceDetailsId(Integer doctorAdvanceDetailsId) {
		this.doctorAdvanceDetailsId = doctorAdvanceDetailsId;
	}

	public Double getRequestedAdvance() {
		return requestedAdvance;
	}

	public void setRequestedAdvance(Double requestedAdvance) {
		this.requestedAdvance = requestedAdvance;
	}

	public Double getApprovedAdvance() {
		return approvedAdvance;
	}

	public void setApprovedAdvance(Double approvedAdvance) {
		this.approvedAdvance = approvedAdvance;
	}

	public Integer getDoctorAdvanceId() {
		return doctorAdvanceId;
	}

	public void setDoctorAdvanceId(Integer doctorAdvanceId) {
		this.doctorAdvanceId = doctorAdvanceId;
	}

	public Double getTotalAdvance() {
		return totalAdvance;
	}

	public void setTotalAdvance(Double totalAdvance) {
		this.totalAdvance = totalAdvance;
	}

	public Double getBalanceAdvance() {
		return balanceAdvance;
	}

	public void setBalanceAdvance(Double balanceAdvance) {
		this.balanceAdvance = balanceAdvance;
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

	public Integer getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Integer requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
