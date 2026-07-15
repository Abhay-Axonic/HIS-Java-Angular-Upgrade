package com.param.global.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.param.opd.encounter.dto.EncounterMasterDto;

public class PatientConsentMapperDto {
	
	private Integer consentTransactionId;
	
	private Integer consentMasterId;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private String templateDesc;
	
	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer consentTypeId;
	
	private String consentMasterName;
	
	private String code;
	
	private String query;
	
	private Integer patientId;
	
	private Integer appointmentId;
	
	private Integer orderDetailsId;
	
	private String templateOriginal;
	
	private String documentName;
				
	private String documentPath;
	
	private String videoPath;
	
	private Boolean isFromConsentPrint;
	
	private MultipartFile file;
	
	private Integer templateType;
	
	private String consentTypeMasterName;

	private String visitNumber;
	
	private Integer labSampleDtlsId;
	
	private String templateFormUrl;
	
	private String conversationId;
	
	private String age;
	
	private String genderName;
	
	private String formTemplateId;
	
	private Integer visitTypeId;
	
	private String mobileNumber;
	
	private String htmlTemplateData;
	
	private Integer templeteTypeId;
	
	private Integer batchId;
	
	private String prnCount;
	
	private String wristBandPrnSize;
	
	private Boolean bonusBatchFlag;
	
	private String batchNo;
	
	private String grnBatchNo;
	
	private String bonusBatchExpiry;
	
	private Boolean barcodeScreen;
	private Integer reportEntryBy;
	private Integer validatedBy;


	public String getBonusBatchExpiry() {
		return bonusBatchExpiry;
	}

	public void setBonusBatchExpiry(String bonusBatchExpiry) {
		this.bonusBatchExpiry = bonusBatchExpiry;
	}

	public String getGrnBatchNo() {
		return grnBatchNo;
	}

	public void setGrnBatchNo(String grnBatchNo) {
		this.grnBatchNo = grnBatchNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	private List<EncounterMasterDto> bonusBatchList;
	
	public List<EncounterMasterDto> getBonusBatchList() {
		return bonusBatchList;
	}

	public void setBonusBatchList(List<EncounterMasterDto> bonusBatchList) {
		this.bonusBatchList = bonusBatchList;
	}

	public Boolean getBonusBatchFlag() {
		return bonusBatchFlag;
	}

	public void setBonusBatchFlag(Boolean bonusBatchFlag) {
		this.bonusBatchFlag = bonusBatchFlag;
	}

	public String getGrnId() {
		return grnId;
	}

	public void setGrnId(String grnId) {
		this.grnId = grnId;
	}

	private String grnId;
	
	public String getHtmlTemplateData() {
		return htmlTemplateData;
	}

	public void setHtmlTemplateData(String htmlTemplateData) {
		this.htmlTemplateData = htmlTemplateData;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public String getTemplateOriginal() {
		return templateOriginal;
	}

	public void setTemplateOriginal(String templateOriginal) {
		this.templateOriginal = templateOriginal;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getConsentTransactionId() {
		return consentTransactionId;
	}

	public void setConsentTransactionId(Integer consentTransactionId) {
		this.consentTransactionId = consentTransactionId;
	}

	public Integer getConsentMasterId() {
		return consentMasterId;
	}

	public void setConsentMasterId(Integer consentMasterId) {
		this.consentMasterId = consentMasterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getTemplateDesc() {
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getConsentTypeId() {
		return consentTypeId;
	}

	public void setConsentTypeId(Integer consentTypeId) {
		this.consentTypeId = consentTypeId;
	}

	public String getConsentMasterName() {
		return consentMasterName;
	}

	public void setConsentMasterName(String consentMasterName) {
		this.consentMasterName = consentMasterName;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public Boolean getIsFromConsentPrint() {
		return isFromConsentPrint;
	}

	public void setIsFromConsentPrint(Boolean isFromConsentPrint) {
		this.isFromConsentPrint = isFromConsentPrint;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public String getConsentTypeMasterName() {
		return consentTypeMasterName;
	}

	public void setConsentTypeMasterName(String consentTypeMasterName) {
		this.consentTypeMasterName = consentTypeMasterName;
	}

	public String getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(String visitNumber) {
		this.visitNumber = visitNumber;
	}

	public String getTemplateFormUrl() {
		return templateFormUrl;
	}

	public void setTemplateFormUrl(String templateFormUrl) {
		this.templateFormUrl = templateFormUrl;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public String getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getTempleteTypeId() {
		return templeteTypeId;
	}

	public void setTempleteTypeId(Integer templeteTypeId) {
		this.templeteTypeId = templeteTypeId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getPrnCount() {
		return prnCount;
	}

	public void setPrnCount(String prnCount) {
		this.prnCount = prnCount;
	}

	public String getWristBandPrnSize() {
		return wristBandPrnSize;
	}

	public void setWristBandPrnSize(String wristBandPrnSize) {
		this.wristBandPrnSize = wristBandPrnSize;
	}

	public Boolean getBarcodeScreen() {
		return barcodeScreen;
	}

	public void setBarcodeScreen(Boolean barcodeScreen) {
		this.barcodeScreen = barcodeScreen;
	}

	public Integer getReportEntryBy() {
		return reportEntryBy;
	}

	public void setReportEntryBy(Integer reportEntryBy) {
		this.reportEntryBy = reportEntryBy;
	}

	public Integer getValidatedBy() {
		return validatedBy;
	}

	public void setValidatedBy(Integer validatedBy) {
		this.validatedBy = validatedBy;
	}
}
