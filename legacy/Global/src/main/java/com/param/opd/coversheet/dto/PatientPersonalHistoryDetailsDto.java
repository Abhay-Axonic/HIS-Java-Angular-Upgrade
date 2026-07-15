package com.param.opd.coversheet.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;


public class PatientPersonalHistoryDetailsDto {

	private Integer personalHistoryDetailsId;
	private String description;
	private String remark;
	private Integer encounterId;
	private Integer patientId;
	private char isNoSignificantHistoryStatus;
	private Integer organizationId;
	private Integer unitId;
	private char status;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String createdDate1;
	private Date createdDateNew;
	private String updatedDate;
	private String fromDate;
	private String toDate;
	private Integer roleId;
	private char isReviewedFlag;
	private char isEnterInErrorStatus;
	private Integer admissionId;
	private List<ListOfPatientPersonalHistoryDto> listOfPatientPersonalHistoryDtos;
	private Integer covTemplateId;
	private String resolvedDate;
	private String resolvedRemark;
	private String userName;
	
	
	
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
	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}
	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
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
	public List<ListOfPatientPersonalHistoryDto> getListOfPatientPersonalHistoryDtos() {
		return listOfPatientPersonalHistoryDtos;
	}
	public void setListOfPatientPersonalHistoryDtos(
			List<ListOfPatientPersonalHistoryDto> listOfPatientPersonalHistoryDtos) {
		this.listOfPatientPersonalHistoryDtos = listOfPatientPersonalHistoryDtos;
	}
	public Integer getPersonalHistoryDetailsId() {
		return personalHistoryDetailsId;
	}
	public void setPersonalHistoryDetailsId(Integer personalHistoryDetailsId) {
		this.personalHistoryDetailsId = personalHistoryDetailsId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public char getIsNoSignificantHistoryStatus() {
		return isNoSignificantHistoryStatus;
	}
	public void setIsNoSignificantHistoryStatus(char isNoSignificantHistoryStatus) {
		this.isNoSignificantHistoryStatus = (isNoSignificantHistoryStatus != '\u0000' ? isNoSignificantHistoryStatus : 'Y');
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
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
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
	public String getResolvedDate() {
		return resolvedDate;
	}
	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}
	public String getResolvedRemark() {
		return resolvedRemark;
	}
	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
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
	
	
}
