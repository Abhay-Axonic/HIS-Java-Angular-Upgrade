package com.param.global.unit.dto;

public class LetterAttachmentMapperDto {

	private int letterAttachmentMapperId;
	
	private Integer appointmentId;
	
	private Integer letterId;
	
	private String  letterDesc;
	
	private String attachmentFilePath;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

	private Integer doctorPromptMapperId;
	
    private String	promptCategory;
    
    private String promptText;
    
    private String casesheetText;
    
    private String aiLetter;
	
    private Integer direactSaveFlage;
    
    private Integer tabId;

    private Integer organizationId;
    
    private Integer unitId;
    
    private Integer patientId;
    
    private Integer userId;
    
    private Boolean isSendTomail;
    
    public int getLetterAttachmentMapperId() {
		return letterAttachmentMapperId;
	}

	public void setLetterAttachmentMapperId(int letterAttachmentMapperId) {
		this.letterAttachmentMapperId = letterAttachmentMapperId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getLetterId() {
		return letterId;
	}

	public void setLetterId(Integer letterId) {
		this.letterId = letterId;
	}

	public String getAttachmentFilePath() {
		return attachmentFilePath;
	}

	public void setAttachmentFilePath(String attachmentFilePath) {
		this.attachmentFilePath = attachmentFilePath;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getLetterDesc() {
		return letterDesc;
	}

	public void setLetterDesc(String letterDesc) {
		this.letterDesc = letterDesc;
	}

	public Integer getDoctorPromptMapperId() {
		return doctorPromptMapperId;
	}

	public void setDoctorPromptMapperId(Integer doctorPromptMapperId) {
		this.doctorPromptMapperId = doctorPromptMapperId;
	}

	public String getPromptCategory() {
		return promptCategory;
	}

	public void setPromptCategory(String promptCategory) {
		this.promptCategory = promptCategory;
	}

	public String getPromptText() {
		return promptText;
	}

	public void setPromptText(String promptText) {
		this.promptText = promptText;
	}

	public String getCasesheetText() {
		return casesheetText;
	}

	public void setCasesheetText(String casesheetText) {
		this.casesheetText = casesheetText;
	}

	public Integer getDireactSaveFlage() {
		return direactSaveFlage;
	}

	public void setDireactSaveFlage(Integer direactSaveFlage) {
		this.direactSaveFlage = direactSaveFlage;
	}

	public String getAiLetter() {
		return aiLetter;
	}

	public void setAiLetter(String aiLetter) {
		this.aiLetter = aiLetter;
	}

	public Integer getTabId() {
		return tabId;
	}

	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	public Boolean getIsSendTomail() {
		return isSendTomail;
	}

	public void setIsSendTomail(Boolean isSendTomail) {
		this.isSendTomail = isSendTomail;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}	
	
	
	
}

