package com.param.global.unit.dto;



import java.util.List;

public class TemplateDiagnosisMapperDto {
	
	private Integer templateDiagnosisMapperId;
	
	private Integer templateId;

	private Integer diagnosisId;
	
	private Character status;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
		
	private String updatedDate;
	
	private Character isDeleted;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private String title;
	
	private String diagnosisDesc;
	
	private String diagnosisName;


	private List<Integer> selectTemplateDiagnosisMapperList;

	private Integer prevTemplateId;

	public Integer getTemplateDiagnosisMapperId() {
		return templateDiagnosisMapperId;
	}

	public void setTemplateDiagnosisMapperId(Integer templateDiagnosisMapperId) {
		this.templateDiagnosisMapperId = templateDiagnosisMapperId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public List<Integer> getSelectTemplateDiagnosisMapperList() {
		return selectTemplateDiagnosisMapperList;
	}

	public void setSelectTemplateDiagnosisMapperList(List<Integer> selectTemplateDiagnosisMapperList) {
		this.selectTemplateDiagnosisMapperList = selectTemplateDiagnosisMapperList;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiagnosisDesc() {
		return diagnosisDesc;
	}

	public void setDiagnosisDesc(String diagnosisDesc) {
		this.diagnosisDesc = diagnosisDesc;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public Integer getPrevTemplateId() {
		return prevTemplateId;
	}

	public void setPrevTemplateId(Integer prevTemplateId) {
		this.prevTemplateId = prevTemplateId;
	}

	
}
