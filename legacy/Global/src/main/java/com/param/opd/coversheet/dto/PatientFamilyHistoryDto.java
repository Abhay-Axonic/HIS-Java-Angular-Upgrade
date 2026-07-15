package com.param.opd.coversheet.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;



public class PatientFamilyHistoryDto {

	private Integer familyHistoryId;
	private char status;
	private String remark;
	private char isNoFamilyHistoryStatus;
	private Integer relationId;
	private Integer diagnosisId;
	private Integer patientId;
	private Integer organizationId;
	private Integer unitId;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private Date createdDateNew;
	private String updatedDate;
	
	private String diagnosisCode;
	private String diagnosisName;
	private String desc;
	private String userName;
	private String fromDate;
	private String toDate;
	private Integer encounterId;
	private Integer admissionId;
	private Integer roleId;
	private char isReviewedFlag;
	private char isEnterInErrorStatus;
	private Integer covTemplateId;
	private String createdDate1;
	private String uuid;
	private Integer hospitalId;
	private List<ListOfPatientFamilyHistoryDto> listOfPatientFamilyHistoryDtos;
	private List<PatientFamilyHistoryDto> patientFamilyHistoryDtoList;
	private String relationDesc;
	private String diagnosisDesc;
	private Integer applicationId;


	
	
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public char getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}
	public void setIsEnterInErrorStatus(char isEnterInErrorStatus) {
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
	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}
	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}
	public String getDiagnosisCode() {
		return diagnosisCode;
	}
	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getFamilyHistoryId() {
		return familyHistoryId;
	}
	public void setFamilyHistoryId(Integer familyHistoryId) {
		this.familyHistoryId = familyHistoryId;
	}
	public List<ListOfPatientFamilyHistoryDto> getListOfPatientFamilyHistoryDtos() {
		return listOfPatientFamilyHistoryDtos;
	}
	public void setListOfPatientFamilyHistoryDtos(
			List<ListOfPatientFamilyHistoryDto> listOfPatientFamilyHistoryDtos) {
		this.listOfPatientFamilyHistoryDtos = listOfPatientFamilyHistoryDtos;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public char getIsNoFamilyHistoryStatus() {
		return isNoFamilyHistoryStatus;
	}
	public void setIsNoFamilyHistoryStatus(char isNoFamilyHistoryStatus) {
		this.isNoFamilyHistoryStatus = isNoFamilyHistoryStatus;
	}
	public Integer getRelationId() {
		return relationId;
	}
	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}
	public Integer getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<PatientFamilyHistoryDto> getPatientFamilyHistoryDtoList() {
		return patientFamilyHistoryDtoList;
	}

	public void setPatientFamilyHistoryDtoList(List<PatientFamilyHistoryDto> patientFamilyHistoryDtoList) {
		this.patientFamilyHistoryDtoList = patientFamilyHistoryDtoList;
	}

	public String getRelationDesc() {
		return relationDesc;
	}

	public void setRelationDesc(String relationDesc) {
		this.relationDesc = relationDesc;
	}

	public String getDiagnosisDesc() {
		return diagnosisDesc;
	}

	public void setDiagnosisDesc(String diagnosisDesc) {
		this.diagnosisDesc = diagnosisDesc;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
}

