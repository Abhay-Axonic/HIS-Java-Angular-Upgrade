package com.param.global.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PatientDialysisRecordDto {
	private String dialysisStartDate;
	private String dialysisStartTime;
	private String dialysisEndDate;
	private String dialysisEndTime;
	private Integer dialysisEventId;
	private Integer dialysisStatusId;
	private String dialysisDuration;
	private String vascularAccessType;
	private String ktvResult;
	private Integer netUltrafication;
	private String dialysisNote;
	private Integer organizationId;
	private Integer unitId;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String updatedDate;
	private Character status;
	private Integer patientId;
	private Integer appointmentId;
	private String documentPath;
	private Integer templateTransactionId;
	private Integer dialysisAppMapperId;
	private Integer patientDialysisRecId;
	private TemplateTransactionDto templateObject;
	private String orgFolderPath;
	private Integer selectedType;
	private String htmlTemplateObject;
    private List<PatientDialysisVitalDto> dialysisVitalList;
    private List<PatientDialysisRecordDto> dialysisPatientList;
    private PatientDialysisRecordDto patientDialysisDetails;
    private String templateTitle;
    private Integer orderDetailsId;
    
	@JsonIgnore
	private MultipartFile file;
	
	public String getDialysisStartTime() {
		return dialysisStartTime;
	}
	public void setDialysisStartTime(String dialysisStartTime) {
		this.dialysisStartTime = dialysisStartTime;
	}
	public String getDialysisEndTime() {
		return dialysisEndTime;
	}
	public void setDialysisEndTime(String dialysisEndTime) {
		this.dialysisEndTime = dialysisEndTime;
	}
	public Integer getDialysisEventId() {
		return dialysisEventId;
	}
	public void setDialysisEventId(Integer dialysisEventId) {
		this.dialysisEventId = dialysisEventId;
	}
	public Integer getDialysisStatusId() {
		return dialysisStatusId;
	}
	public void setDialysisStatusId(Integer dialysisStatusId) {
		this.dialysisStatusId = dialysisStatusId;
	}
	public String getDialysisDuration() {
		return dialysisDuration;
	}
	public void setDialysisDuration(String dialysisDuration) {
		this.dialysisDuration = dialysisDuration;
	}
	public String getVascularAccessType() {
		return vascularAccessType;
	}
	public void setVascularAccessType(String vascularAccessType) {
		this.vascularAccessType = vascularAccessType;
	}
	public String getKtvResult() {
		return ktvResult;
	}
	public void setKtvResult(String ktvResult) {
		this.ktvResult = ktvResult;
	}
	public Integer getNetUltrafication() {
		return netUltrafication;
	}
	public void setNetUltrafication(Integer netUltrafication) {
		this.netUltrafication = netUltrafication;
	}
	public String getDialysisNote() {
		return dialysisNote;
	}
	public void setDialysisNote(String dialysisNote) {
		this.dialysisNote = dialysisNote;
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
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getDocumentPath() {
		return documentPath;
	}
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
	public TemplateTransactionDto getTemplateObject() {
		return templateObject;
	}
	public void setTemplateObject(TemplateTransactionDto templateObject) {
		this.templateObject = templateObject;
	}
	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}
	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}
	public Integer getDialysisAppMapperId() {
		return dialysisAppMapperId;
	}
	public void setDialysisAppMapperId(Integer dialysisAppMapperId) {
		this.dialysisAppMapperId = dialysisAppMapperId;
	}
	public Integer getPatientDialysisRecId() {
		return patientDialysisRecId;
	}
	public void setPatientDialysisRecId(Integer patientDialysisRecId) {
		this.patientDialysisRecId = patientDialysisRecId;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getOrgFolderPath() {
		return orgFolderPath;
	}
	public void setOrgFolderPath(String orgFolderPath) {
		this.orgFolderPath = orgFolderPath;
	}
	public Integer getSelectedType() {
		return selectedType;
	}
	public void setSelectedType(Integer selectedType) {
		this.selectedType = selectedType;
	}
	public String getDialysisStartDate() {
		return dialysisStartDate;
	}
	public void setDialysisStartDate(String dialysisStartDate) {
		this.dialysisStartDate = dialysisStartDate;
	}
	public String getHtmlTemplateObject() {
		return htmlTemplateObject;
	}
	public void setHtmlTemplateObject(String htmlTemplateObject) {
		this.htmlTemplateObject = htmlTemplateObject;
	}
	public List<PatientDialysisVitalDto> getDialysisVitalList() {
		return dialysisVitalList;
	}
	public void setDialysisVitalList(List<PatientDialysisVitalDto> dialysisVitalList) {
		this.dialysisVitalList = dialysisVitalList;
	}
	public List<PatientDialysisRecordDto> getDialysisPatientList() {
		return dialysisPatientList;
	}
	public void setDialysisPatientList(List<PatientDialysisRecordDto> dialysisPatientList) {
		this.dialysisPatientList = dialysisPatientList;
	}
	public String getDialysisEndDate() {
		return dialysisEndDate;
	}
	public void setDialysisEndDate(String dialysisEndDate) {
		this.dialysisEndDate = dialysisEndDate;
	}
	public PatientDialysisRecordDto getPatientDialysisDetails() {
		return patientDialysisDetails;
	}
	public void setPatientDialysisDetails(PatientDialysisRecordDto patientDialysisDetails) {
		this.patientDialysisDetails = patientDialysisDetails;
	}
	public String getTemplateTitle() {
		return templateTitle;
	}
	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	
}
