package com.param.opd.coversheet.dto;

import java.util.Date;
import java.util.List;

public class PatientTreatmentHistoryDetailsDto {
	private Integer treatmentHistoryId;

	private String remark;
	
	private Character isNoSignificantStatus;
	
	private Integer month;
	
	private Integer year;
	
	private String treatementName;
	
	private Character status;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer roleId;
	
	private Character isReviewedFlag;
	
	private Character isEnterInErrorStatus;
	
	private Integer patientId;
	
	private String surgenName;
	
	private Integer organizationId;
	
	private Integer unitId;

	private Integer createdBy;

	private Integer updatedBy;

	private String createdDate;
	private String createdDate1;
	private Date createdDateNew;

	private String updatedDate;
	private Integer orderDetailsId;
	private String userName;
	private List<PatientTreatmentHistoryDetailsDto> listOfPatientTreatmentHistoryDto;
	private String fromDate;
	private String toDate;
	private String monthProc;
	private String yearProc;
	private String treatDate;
	private Date treatDateNew;
	private Integer covTemplateId;
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getIsNoSignificantStatus() {
		return isNoSignificantStatus;
	}

	public void setIsNoSignificantStatus(Character isNoSignificantStatus) {
		this.isNoSignificantStatus = isNoSignificantStatus;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getTreatementName() {
		return treatementName;
	}

	public void setTreatementName(String treatementName) {
		this.treatementName = treatementName;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Character getIsReviewedFlag() {
		return isReviewedFlag;
	}

	public void setIsReviewedFlag(Character isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}

	public Character getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}

	public void setIsEnterInErrorStatus(Character isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	
	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public List<PatientTreatmentHistoryDetailsDto> getListOfPatientTreatmentHistoryDto() {
		return listOfPatientTreatmentHistoryDto;
	}

	public void setListOfPatientTreatmentHistoryDto(
			List<PatientTreatmentHistoryDetailsDto> listOfPatientTreatmentHistoryDto) {
		this.listOfPatientTreatmentHistoryDto = listOfPatientTreatmentHistoryDto;
	}

	public Integer getTreatmentHistoryId() {
		return treatmentHistoryId;
	}

	public void setTreatmentHistoryId(Integer treatmentHistoryId) {
		this.treatmentHistoryId = treatmentHistoryId;
	}

	public String getSurgenName() {
		return surgenName;
	}

	public void setSurgenName(String surgenName) {
		this.surgenName = surgenName;
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

	public String getTreatDate() {
		return treatDate;
	}

	public void setTreatDate(String treatDate) {
		this.treatDate = treatDate;
	}

	public String getMonthProc() {
		return monthProc;
	}

	public void setMonthProc(String monthProc) {
		this.monthProc = monthProc;
	}

	public String getYearProc() {
		return yearProc;
	}

	public void setYearProc(String yearProc) {
		this.yearProc = yearProc;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public Date getCreatedDateNew() {
		return createdDateNew;
	}

	public void setCreatedDateNew(Date createdDateNew) {
		this.createdDateNew = createdDateNew;
	}

	public Date getTreatDateNew() {
		return treatDateNew;
	}

	public void setTreatDateNew(Date treatDateNew) {
		this.treatDateNew = treatDateNew;
	}

	public String getCreatedDate1() {
		return createdDate1;
	}

	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}

	
	
}
