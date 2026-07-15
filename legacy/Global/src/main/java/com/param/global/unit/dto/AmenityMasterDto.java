package com.param.global.unit.dto;

public class AmenityMasterDto {
	
	private Integer amenityId;
	
	private Integer unitId;
	
	private Integer organizationId;

	private String amenityCode;
	
	private String amenityDesc;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;

	public Integer getAmenityId() {
		return amenityId;
	}

	public void setAmenityId(Integer amenityId) {
		this.amenityId = amenityId;
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

	public String getAmenityCode() {
		return amenityCode;
	}

	public void setAmenityCode(String amenityCode) {
		this.amenityCode = amenityCode;
	}

	public String getAmenityDesc() {
		return amenityDesc;
	}

	public void setAmenityDesc(String amenityDesc) {
		this.amenityDesc = amenityDesc;
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
