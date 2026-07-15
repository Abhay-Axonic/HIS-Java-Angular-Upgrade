package com.param.global.dto;


public class ExaminationMasterDto {
	
	private Integer examinationMasterId;

	private String examinationCode;

	private String examinationDesc;

	private Character status;

	private Integer organizationId;

	private Integer unitId;

	private Integer genderId;

	private Integer createdBy;

	private Integer updatedBy;

	private String createdDate;

	private String updatedDate;
	private Integer offset;
	private String searchKeyword;

	private Integer noOfRecordsPerPage;
	public Integer getExaminationMasterId() {
		return examinationMasterId;
	}

	public void setExaminationMasterId(Integer examinationMasterId) {
		this.examinationMasterId = examinationMasterId;
	}

	public String getExaminationCode() {
		return examinationCode;
	}

	public void setExaminationCode(String examinationCode) {
		this.examinationCode = examinationCode;
	}

	public String getExaminationDesc() {
		return examinationDesc;
	}

	public void setExaminationDesc(String examinationDesc) {
		this.examinationDesc = examinationDesc;
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

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	
}
