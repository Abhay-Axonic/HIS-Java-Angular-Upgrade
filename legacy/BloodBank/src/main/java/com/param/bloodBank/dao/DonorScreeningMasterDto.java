package com.param.bloodBank.dao;

import java.util.List;

import com.param.global.dto.GenderMasterDto;

public class DonorScreeningMasterDto {

	private Integer id;
	private String parameter;
	private Integer deferredTypeId;
	private String deferredType;
	private Integer genderId;
	private String gender;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String updatedDate;
	private Character status;
	private Integer organizationId;
	private Integer unitId;
	private List<GenderMasterDto> genderList;
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public Integer getDeferredTypeId() {
		return deferredTypeId;
	}
	public void setDeferredTypeId(Integer deferredTypeId) {
		this.deferredTypeId = deferredTypeId;
	}
	public String getDeferredType() {
		return deferredType;
	}
	public void setDeferredType(String deferredType) {
		this.deferredType = deferredType;
	}
	
	public List<GenderMasterDto> getGenderList() {
		return genderList;
	}
	public void setGenderList(List<GenderMasterDto> genderList) {
		this.genderList = genderList;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
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
	
	
}
