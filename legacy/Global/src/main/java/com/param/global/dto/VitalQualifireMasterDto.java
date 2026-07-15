package com.param.global.dto;

import java.util.List;

public class VitalQualifireMasterDto {

	private Integer vitalQualifireId;
	
	private Integer vitalCategoryId;
	
	private String vitalQualifireCode;
	
	private String vitalQualifireDesc;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	private Integer vital_qualifire_id;
	private Integer vitalId;

	private List<VitalQualifireMasterDto> ListOfVitalQualifires;
	private String vitalCategoryDesc;
	private Integer offset;
	private Integer noOfRecordsPerPage;
	
	public Integer getVitalQualifireId() {
		return vitalQualifireId;
	}

	public void setVitalQualifireId(Integer vitalQualifireId) {
		this.vitalQualifireId = vitalQualifireId;
	}

	public Integer getVitalCategoryId() {
		return vitalCategoryId;
	}

	public void setVitalCategoryId(Integer vitalCategoryId) {
		this.vitalCategoryId = vitalCategoryId;
	}

	public String getVitalQualifireCode() {
		return vitalQualifireCode;
	}

	public void setVitalQualifireCode(String vitalQualifireCode) {
		this.vitalQualifireCode = vitalQualifireCode;
	}

	public String getVitalQualifireDesc() {
		return vitalQualifireDesc;
	}

	public void setVitalQualifireDesc(String vitalQualifireDesc) {
		this.vitalQualifireDesc = vitalQualifireDesc;
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


	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getVitalCategoryDesc() {
		return vitalCategoryDesc;
	}

	public void setVitalCategoryDesc(String vitalCategoryDesc) {
		this.vitalCategoryDesc = vitalCategoryDesc;
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

	public Integer getVital_qualifire_id() {
		return vital_qualifire_id;
	}

	public void setVital_qualifire_id(Integer vital_qualifire_id) {
		this.vital_qualifire_id = vital_qualifire_id;
	}

	public List<VitalQualifireMasterDto> getListOfVitalQualifires() {
		return ListOfVitalQualifires;
	}

	public void setListOfVitalQualifires(List<VitalQualifireMasterDto> listOfVitalQualifires) {
		ListOfVitalQualifires = listOfVitalQualifires;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}
}
