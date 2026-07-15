package com.param.opd.encounter.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class EncounterDocDetailsDto {

	private Integer encounterDocId;

	private Integer encounterId;

	private Integer patientId;

	private String documentPath;

	private int documentTypeId;

	private char status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private Integer unitId;

	private Integer organisationId;

	private MultipartFile file;
	
	private Integer visitTypeId;
	
	private Integer admissionId;
	
	private Integer preauthRequestId;
	
	private Integer labSampleDtlsId;
	
	private String fileName;
	
	private String uploadDoccumentDate;
	
	private Integer expenseId;
	
	private Integer grnId;
	
	private Integer incidentReportingMapperId;
	
	private Integer companyExpenseId;
	
	private String fileContent;
	
	private String remark;
	
	private Integer poId;
	
	private Integer deathPatientId;
	
	private String fileEditedName;

	private Integer opbId;
	
	private Integer vendorId;
	
	private String endpoint;
	
	private String accessKey;
	
	private String secretKey;
	
	private String bucketName;
	
	private Integer nabhReportingMapperId;

	private String callBackBaseUrl;

	private String documentName;

	private String videolink;

	private String video_url;

	private String script;

	private Object ai_analysis;

	private Date AnalyzedDocDate;

	private String report_id;

	private Object original_input;

	private String ai_analysis_raw;

	private String videoStatus;

	private String latestMedicalSummaryReport;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCompanyExpenseId() {
		return companyExpenseId;
	}

	public void setCompanyExpenseId(Integer companyExpenseId) {
		this.companyExpenseId = companyExpenseId;
	}

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getEncounterDocId() {
		return encounterDocId;
	}

	public void setEncounterDocId(Integer encounterDocId) {
		this.encounterDocId = encounterDocId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'A');
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPreauthRequestId() {
		return preauthRequestId;
	}

	public void setPreauthRequestId(Integer preauthRequestId) {
		this.preauthRequestId = preauthRequestId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public String getUploadDoccumentDate() {
		return uploadDoccumentDate;
	}

	public void setUploadDoccumentDate(String uploadDoccumentDate) {
		this.uploadDoccumentDate = uploadDoccumentDate;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getIncidentReportingMapperId() {
		return incidentReportingMapperId;
	}

	public void setIncidentReportingMapperId(Integer incidentReportingMapperId) {
		this.incidentReportingMapperId = incidentReportingMapperId;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getDeathPatientId() {
		return deathPatientId;
	}

	public void setDeathPatientId(Integer deathPatientId) {
		this.deathPatientId = deathPatientId;
	}

	public String getFileEditedName() {
		return fileEditedName;
	}

	public void setFileEditedName(String fileEditedName) {
		this.fileEditedName = fileEditedName;
	}

	public Integer getOpbId() {
		return opbId;
	}

	public void setOpbId(Integer opbId) {
		this.opbId = opbId;
	}
	
	public Integer getVendorId(){
		return vendorId;
	}
	
	public void setVendorId(Integer vendorId){
		this.vendorId = vendorId;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public Integer getNabhReportingMapperId() {
		return nabhReportingMapperId;
	}

	public void setNabhReportingMapperId(Integer nabhReportingMapperId) {
		this.nabhReportingMapperId = nabhReportingMapperId;
	}

	public String getCallBackBaseUrl() {
		return callBackBaseUrl;
	}

	public void setCallBackBaseUrl(String callBackBaseUrl) {
		this.callBackBaseUrl = callBackBaseUrl;
	}


	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getVideolink() {
		return videolink;
	}

	public void setVideolink(String videolink) {
		this.videolink = videolink;
	}

	public Object getAi_analysis() {
		return ai_analysis;
	}

	public void setAi_analysis(Object ai_analysis) {
		this.ai_analysis = ai_analysis;
	}

	public Date getAnalyzedDocDate() {
		return AnalyzedDocDate;
	}

	public void setAnalyzedDocDate(Date analyzedDocDate) {
		AnalyzedDocDate = analyzedDocDate;
	}

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public Object getOriginal_input() {
		return original_input;
	}

	public void setOriginal_input(Object original_input) {
		this.original_input = original_input;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getAi_analysis_raw() {
		return ai_analysis_raw;
	}

	public void setAi_analysis_raw(String ai_analysis_raw) {
		this.ai_analysis_raw = ai_analysis_raw;
	}

	public String getVideoStatus() {
		return videoStatus;
	}

	public void setVideoStatus(String videoStatus) {
		this.videoStatus = videoStatus;
	}

	public String getLatestMedicalSummaryReport() {
		return latestMedicalSummaryReport;
	}

	public void setLatestMedicalSummaryReport(String latestMedicalSummaryReport) {
		this.latestMedicalSummaryReport = latestMedicalSummaryReport;
	}
}
