package com.param.global.dto;

import java.util.List;


public class VitalQualifireMapperDto {

	private Integer vitalQualifireMapperId;
	
	private Integer vitalId;
	
	private Integer vitalQualifireId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	private Integer vitalCategoryId;
	private Character isDeleted;
	private String updatedDate;
	private Integer offset;
	private Integer noOfRecordsPerPage;
	private List<VitalQualifireMapperDto> listOfVitalQualifireMapperDtoS;
	private List<VitalQualifireMapperDto> listOfVitalQualifiresForCategory;
	private List<VitalQualifireMapperDto> listOfVitalQualifiresMapperForQualifires;
	private String vitalName;
	private String vitalCategoryDesc;
	private String vitalQualifireDesc;

	public Integer getVitalQualifireMapperId() {
		return vitalQualifireMapperId;
	}

	public void setVitalQualifireMapperId(Integer vitalQualifireMapperId) {
		this.vitalQualifireMapperId = vitalQualifireMapperId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
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

	public List<VitalQualifireMapperDto> getListOfVitalQualifireMapperDtoS() {
		return listOfVitalQualifireMapperDtoS;
	}

	public void setListOfVitalQualifireMapperDtoS(List<VitalQualifireMapperDto> listOfVitalQualifireMapperDtoS) {
		this.listOfVitalQualifireMapperDtoS = listOfVitalQualifireMapperDtoS;
	}

	public List<VitalQualifireMapperDto> getListOfVitalQualifiresForCategory() {
		return listOfVitalQualifiresForCategory;
	}

	public void setListOfVitalQualifiresForCategory(List<VitalQualifireMapperDto> listOfVitalQualifiresForCategory) {
		this.listOfVitalQualifiresForCategory = listOfVitalQualifiresForCategory;
	}

	public List<VitalQualifireMapperDto> getListOfVitalQualifiresMapperForQualifires() {
		return listOfVitalQualifiresMapperForQualifires;
	}

	public void setListOfVitalQualifiresMapperForQualifires(List<VitalQualifireMapperDto> listOfVitalQualifiresMapperForQualifires) {
		this.listOfVitalQualifiresMapperForQualifires = listOfVitalQualifiresMapperForQualifires;
	}

	public String getVitalName() {
		return vitalName;
	}

	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}

	public String getVitalCategoryDesc() {
		return vitalCategoryDesc;
	}

	public void setVitalCategoryDesc(String vitalCategoryDesc) {
		this.vitalCategoryDesc = vitalCategoryDesc;
	}

	public String getVitalQualifireDesc() {
		return vitalQualifireDesc;
	}

	public void setVitalQualifireDesc(String vitalQualifireDesc) {
		this.vitalQualifireDesc = vitalQualifireDesc;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
