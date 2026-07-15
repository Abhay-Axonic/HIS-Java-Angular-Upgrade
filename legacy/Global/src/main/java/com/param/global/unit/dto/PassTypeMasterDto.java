package com.param.global.unit.dto;


public class PassTypeMasterDto {

	private int visitorPassTypeId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String visitorPassTypeCode;
	
	private String visitorPassTypeDesc;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String createdDate;
	
	private Character status;
	
	
	private Integer offset;

	private Integer noOfRecordsPerPage;

	

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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public int getVisitorPassTypeId() {
		return visitorPassTypeId;
	}

	public void setVisitorPassTypeId(int visitorPassTypeId) {
		this.visitorPassTypeId = visitorPassTypeId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getVisitorPassTypeCode() {
		return visitorPassTypeCode;
	}

	public void setVisitorPassTypeCode(String visitorPassTypeCode) {
		this.visitorPassTypeCode = visitorPassTypeCode;
	}

	public String getVisitorPassTypeDesc() {
		return visitorPassTypeDesc;
	}

	public void setVisitorPassTypeDesc(String visitorPassTypeDesc) {
		this.visitorPassTypeDesc = visitorPassTypeDesc;
	}
	
	
}
