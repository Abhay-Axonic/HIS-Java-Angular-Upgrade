package com.param.global.otims.dto;

import java.util.List;

public class PreOperativeDetailsDto {

	private Integer preOperativeId;
	
	private Integer proReqId;
	
	private String indicationReason;
	
	private Integer patientIdentificationStatus;
	
	private Integer patientConsentStatus;
	
	private Integer whoChecklistStatus;
	
	private Integer bloodStatus;
	
	private Integer icuStatus;
	
	private Integer anaesthesiaStatus;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String email;
	
	private String doctorName;
	
	private String mobileNumber;
	
	private String preferedDateTime;
	
	private String procedureName;
	
	private String theatreName;
	
	private Integer procedureOrderId;
	
	private List<WhoChecklistDto>  whoChecklistDtoList;
	
	private OtCompletionTimeSheetDto otCompletionTimeSheetDto;
	
	private List<ImplantDetailsDto> implantDetailsList;
	
	private List<MedicationDetailsDto> medicationDetailsList;

	private String transcribedText;

	private String userPrompt;

	private String aiTranscriptionApiKey;

	private String aiTranscriptionUrl;

	public Integer getPreOperativeId() {
		return preOperativeId;
	}

	public void setPreOperativeId(Integer preOperativeId) {
		this.preOperativeId = preOperativeId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getIndicationReason() {
		return indicationReason;
	}

	public void setIndicationReason(String indicationReason) {
		this.indicationReason = indicationReason;
	}

	public Integer getPatientIdentificationStatus() {
		return patientIdentificationStatus;
	}

	public void setPatientIdentificationStatus(Integer patientIdentificationStatus) {
		this.patientIdentificationStatus = patientIdentificationStatus;
	}

	public Integer getPatientConsentStatus() {
		return patientConsentStatus;
	}

	public void setPatientConsentStatus(Integer patientConsentStatus) {
		this.patientConsentStatus = patientConsentStatus;
	}

	public Integer getWhoChecklistStatus() {
		return whoChecklistStatus;
	}

	public void setWhoChecklistStatus(Integer whoChecklistStatus) {
		this.whoChecklistStatus = whoChecklistStatus;
	}

	public Integer getBloodStatus() {
		return bloodStatus;
	}

	public void setBloodStatus(Integer bloodStatus) {
		this.bloodStatus = bloodStatus;
	}

	public Integer getIcuStatus() {
		return icuStatus;
	}

	public void setIcuStatus(Integer icuStatus) {
		this.icuStatus = icuStatus;
	}

	public Integer getAnaesthesiaStatus() {
		return anaesthesiaStatus;
	}

	public void setAnaesthesiaStatus(Integer anaesthesiaStatus) {
		this.anaesthesiaStatus = anaesthesiaStatus;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<WhoChecklistDto> getWhoChecklistDtoList() {
		return whoChecklistDtoList;
	}

	public void setWhoChecklistDtoList(List<WhoChecklistDto> whoChecklistDtoList) {
		this.whoChecklistDtoList = whoChecklistDtoList;
	}

	public OtCompletionTimeSheetDto getOtCompletionTimeSheetDto() {
		return otCompletionTimeSheetDto;
	}

	public void setOtCompletionTimeSheetDto(OtCompletionTimeSheetDto otCompletionTimeSheetDto) {
		this.otCompletionTimeSheetDto = otCompletionTimeSheetDto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPreferedDateTime() {
		return preferedDateTime;
	}

	public void setPreferedDateTime(String preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public List<ImplantDetailsDto> getImplantDetailsList() {
		return implantDetailsList;
	}

	public void setImplantDetailsList(List<ImplantDetailsDto> implantDetailsList) {
		this.implantDetailsList = implantDetailsList;
	}

	public List<MedicationDetailsDto> getMedicationDetailsList() {
		return medicationDetailsList;
	}

	public void setMedicationDetailsList(List<MedicationDetailsDto> medicationDetailsList) {
		this.medicationDetailsList = medicationDetailsList;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public String getTranscribedText() {
		return transcribedText;
	}

	public void setTranscribedText(String transcribedText) {
		this.transcribedText = transcribedText;
	}

	public String getUserPrompt() {
		return userPrompt;
	}

	public void setUserPrompt(String userPrompt) {
		this.userPrompt = userPrompt;
	}

	public String getAiTranscriptionApiKey() {
		return aiTranscriptionApiKey;
	}

	public void setAiTranscriptionApiKey(String aiTranscriptionApiKey) {
		this.aiTranscriptionApiKey = aiTranscriptionApiKey;
	}

	public String getAiTranscriptionUrl() {
		return aiTranscriptionUrl;
	}

	public void setAiTranscriptionUrl(String aiTranscriptionUrl) {
		this.aiTranscriptionUrl = aiTranscriptionUrl;
	}
}
