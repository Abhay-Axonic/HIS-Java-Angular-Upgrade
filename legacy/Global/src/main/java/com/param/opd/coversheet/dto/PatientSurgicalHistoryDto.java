package com.param.opd.coversheet.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class PatientSurgicalHistoryDto {

	private Integer surgicalHistoryDetailsId;
	private String remark;
	private Character isNoSignificantStatus;
	private Integer month;
	private Integer year;
	private Integer surgeryId;
	private String surgenName;
	private String userName;
	private Character status;
	private Integer appointmentId;
	private Integer patientId;
	private Integer organizationId;
	private Integer unitId;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String createdDate1;
	private Date createdDateNew;
	private String updatedDate;
	private Integer procedureStatusId;

	private Integer encounterId;
	private Integer admissionId;
	private Integer roleId;
	private Character isReviewedFlag;
	private String monthProc;
	private String yearProc;
	private String surgeryName;
	private String surgeryCode;
	private Integer covTemplateId;
	private Character isEnterInErrorStatus;
	
	private String  diagnosisName;
	private String  surgeryNameFormulary;

	private String fromDate;
	private String toDate;
	private BigInteger countSurg;

	private List<ListOfPatientSurgicalHistoryDto> listOfPatientSurgicalHistoryDto;

	private Integer hospitalId;
	private List<PatientSurgicalHistoryDto> patientSurgicalHistoryDtoList;
	private String uuid;
	private Integer applicationId;

	
	public Character getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}
	public void setIsEnterInErrorStatus(Character isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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
	public String getSurgeryName() {
		return surgeryName;
	}
	public void setSurgeryName(String surgeryName) {
		this.surgeryName = surgeryName;
	}
	public List<ListOfPatientSurgicalHistoryDto> getListOfPatientSurgicalHistoryDto() {
		return listOfPatientSurgicalHistoryDto;
	}
	public void setListOfPatientSurgicalHistoryDto(
			List<ListOfPatientSurgicalHistoryDto> listOfPatientSurgicalHistoryDto) {
		this.listOfPatientSurgicalHistoryDto = listOfPatientSurgicalHistoryDto;
	}
	public Integer getSurgicalHistoryDetailsId() {
		return surgicalHistoryDetailsId;
	}
	public void setSurgicalHistoryDetailsId(Integer surgicalHistoryDetailsId) {
		this.surgicalHistoryDetailsId = surgicalHistoryDetailsId;
	}
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
	public Integer getSurgeryId() {
		return surgeryId;
	}
	public void setSurgeryId(Integer surgeryId) {
		this.surgeryId = surgeryId;
	}
	
	public String getSurgenName() {
		return surgenName;
	}
	public void setSurgenName(String surgenName) {
		this.surgenName = surgenName;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
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
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public String getSurgeryNameFormulary() {
		return surgeryNameFormulary;
	}
	public void setSurgeryNameFormulary(String surgeryNameFormulary) {
		this.surgeryNameFormulary = surgeryNameFormulary;
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
	public Integer getProcedureStatusId() {
		return procedureStatusId;
	}
	public void setProcedureStatusId(Integer procedureStatusId) {
		this.procedureStatusId = procedureStatusId;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigInteger getCountSurg() {
		return countSurg;
	}
	public void setCountSurg(BigInteger countSurg) {
		this.countSurg = countSurg;
	}
	public String getSurgeryCode() {
		return surgeryCode;
	}
	public void setSurgeryCode(String surgeryCode) {
		this.surgeryCode = surgeryCode;
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
	public String getCreatedDate1() {
		return createdDate1;
	}
	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public List<PatientSurgicalHistoryDto> getPatientSurgicalHistoryDtoList() {
		return patientSurgicalHistoryDtoList;
	}

	public void setPatientSurgicalHistoryDtoList(List<PatientSurgicalHistoryDto> patientSurgicalHistoryDtoList) {
		this.patientSurgicalHistoryDtoList = patientSurgicalHistoryDtoList;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
}
