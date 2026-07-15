package com.param.global.dto;

import java.util.List;

public class ICPCMapperDto {
	
	private Integer icpcSystemMapperId;

	private Integer icpcSystemId;

	private Integer unitId;

	private Integer organizationId;

	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Integer genderId;
	
	private Integer id;
	
	private String genderDesc;

	private String icpcSystemDesc;
	private Integer specialityId;
	private String desc;

	private Integer offset;
	private Integer noOfRecordsPerPage;
	private List<ICPCMapperDto> genderList;
	
	public Integer getIcpcSystemMapperId() {
		return icpcSystemMapperId;
	}

	public void setIcpcSystemMapperId(Integer icpcSystemMapperId) {
		this.icpcSystemMapperId = icpcSystemMapperId;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
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

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public List<ICPCMapperDto> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<ICPCMapperDto> genderList) {
		this.genderList = genderList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGenderDesc() {
		return genderDesc;
	}

	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}

	public String getIcpcSystemDesc() {
		return icpcSystemDesc;
	}

	public void setIcpcSystemDesc(String icpcSystemDesc) {
		this.icpcSystemDesc = icpcSystemDesc;
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

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	
	
}
