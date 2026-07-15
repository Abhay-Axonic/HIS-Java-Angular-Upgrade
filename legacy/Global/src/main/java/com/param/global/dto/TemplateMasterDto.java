package com.param.global.dto;

import java.math.BigInteger;

public class TemplateMasterDto {
	private int templateId;
	private String title;
	private Integer specialityId;
	private Integer subSpecialityId;
	private String templateObject;
	private char status;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Integer orgId;
	private Integer unitId;
	private Integer doctorId;
	private String speciality;
	private String subSpeciality;
	private String doctor;
	private Integer templateTypeId;
	private Integer templateFormatId;
	private String templateType;
	private String  templateObjectHtml; //dilip
	private String formTemplateId;
	private String formTemplateKeywords;
	private Boolean isHtmlPrint;	
	private BigInteger count;
	public Integer getTemplateTypeId() {
		return templateTypeId;
	}
	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getSubSpeciality() {
		return subSpeciality;
	}
	public void setSubSpeciality(String subSpeciality) {
		this.subSpeciality = subSpeciality;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public String getTemplateObject() {
		return templateObject;
	}
	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
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
	public Integer getTemplateFormatId() {
		return templateFormatId;
	}
	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
	public String getTemplateObjectHtml(){  //dilip
			return templateObjectHtml;
	}
	
	public void setTemplateObjectHtml(String templateObjectHtml){ //dilip
		this.templateObjectHtml = templateObjectHtml;
	}
	public String getFormTemplateId() {
		return formTemplateId;
	}
	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}
	public String getFormTemplateKeywords() {
		return formTemplateKeywords;
	}
	public void setFormTemplateKeywords(String formTemplateKeywords) {
		this.formTemplateKeywords = formTemplateKeywords;
	}
	public Boolean getIsHtmlPrint() {
		return isHtmlPrint;
	}
	public void setIsHtmlPrint(Boolean isHtmlPrint) {
		this.isHtmlPrint = isHtmlPrint;
	}
	public BigInteger getCount() {
		return count;
	}
	public void setCount(BigInteger count) {
		this.count = count;
	}
	
	
}
