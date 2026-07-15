package com.param.global.service;

public class TemplateQuetionaryPrintDto {
	private Integer templateTransactionId;
	private Integer organizationId;
	private Integer unitId;
	private Integer addedBy;
	private String addedDate;
	private Integer updatedBy;
	private String updatedDate;
	private String summaryForPrint;
	private String queuId;
	private String queTitle;
	private String queType;
	private String answer;
	private String slugValue;
	private String slugValueNew;
	private String sectionTitle;
	private boolean allowToPrint;
	private Character status;
	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}
	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}
	
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	public String getSummaryForPrint() {
		return summaryForPrint;
	}
	public void setSummaryForPrint(String summaryForPrint) {
		this.summaryForPrint = summaryForPrint;
	}
	
	public String getQueuId() {
		return queuId;
	}
	public void setQueuId(String queuId) {
		this.queuId = queuId;
	}
	public String getQueTitle() {
		return queTitle;
	}
	public void setQueTitle(String queTitle) {
		this.queTitle = queTitle;
	}
	public String getQueType() {
		return queType;
	}
	public void setQueType(String queType) {
		this.queType = queType;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getSlugValue() {
		return slugValue;
	}
	public void setSlugValue(String slugValue) {
		this.slugValue = slugValue;
	}
	public String getSlugValueNew() {
		return slugValueNew;
	}
	public void setSlugValueNew(String slugValueNew) {
		this.slugValueNew = slugValueNew;
	}
	public String getSectionTitle() {
		return sectionTitle;
	}
	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}
	public boolean getAllowToPrint() {
		return allowToPrint;
	}
	public void setAllowToPrint(boolean allowToPrint) {
		this.allowToPrint = allowToPrint;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}
	public String getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
}
