package com.param.global.unit.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class PatientTabularTemplateDataMapperDto {

	private Integer patientTabularTemplateDataMapperId;
	
	private Integer tabularTemplateDataMapperId;
	
	private Integer templateFormatId;
	

	private Integer templateTypeId;
	
	private Integer formTemplateId;
	
	private Integer templateId;
	
	private Integer patientId;
	
	private Integer doctorId;
	
	private Integer tabularTemplateId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private String columnName;
	
	private String tableName;
	
	private Integer coulmnId;
	
	private Integer rowId;
	
	private String rowName;
	
	private String inputValue;
	
	private Integer numberOfCoulmn;
	
	private Integer numberOfRow;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String title;
	
	private String userName;
	
	private String doctorName;
	
	private List<PatientTabularTemplateDataMapperDto> toSaveListPatient;
	
	private List<PatientTabularTemplateDataMapperDto> toSaveList;
	
	private boolean isHtmlPrint;

	public Integer getPatientTabularTemplateDataMapperId() {
		return patientTabularTemplateDataMapperId;
	}

	public void setPatientTabularTemplateDataMapperId(Integer patientTabularTemplateDataMapperId) {
		this.patientTabularTemplateDataMapperId = patientTabularTemplateDataMapperId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getTabularTemplateId() {
		return tabularTemplateId;
	}

	public void setTabularTemplateId(Integer tabularTemplateId) {
		this.tabularTemplateId = tabularTemplateId;
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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getCoulmnId() {
		return coulmnId;
	}

	public void setCoulmnId(Integer coulmnId) {
		this.coulmnId = coulmnId;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public Integer getNumberOfCoulmn() {
		return numberOfCoulmn;
	}

	public void setNumberOfCoulmn(Integer numberOfCoulmn) {
		this.numberOfCoulmn = numberOfCoulmn;
	}

	public Integer getNumberOfRow() {
		return numberOfRow;
	}

	public void setNumberOfRow(Integer numberOfRow) {
		this.numberOfRow = numberOfRow;
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

	public List<PatientTabularTemplateDataMapperDto> getToSaveListPatient() {
		return toSaveListPatient;
	}

	public void setToSaveListPatient(List<PatientTabularTemplateDataMapperDto> toSaveListPatient) {
		this.toSaveListPatient = toSaveListPatient;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTabularTemplateDataMapperId() {
		return tabularTemplateDataMapperId;
	}

	public void setTabularTemplateDataMapperId(Integer tabularTemplateDataMapperId) {
		this.tabularTemplateDataMapperId = tabularTemplateDataMapperId;
	}

	public List<PatientTabularTemplateDataMapperDto> getToSaveList() {
		return toSaveList;
	}

	public void setToSaveList(List<PatientTabularTemplateDataMapperDto> toSaveList) {
		this.toSaveList = toSaveList;
	}

	public Integer getTemplateFormatId() {
		return templateFormatId;
	}

	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}

	public boolean getIsHtmlPrint() {
		return isHtmlPrint;
	}
	public void setIsHtmlPrint(boolean isHtmlPrint) {
		this.isHtmlPrint = isHtmlPrint;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(Integer formTemplateId) {
		this.formTemplateId = formTemplateId;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	
}
