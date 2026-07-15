package com.param.billing.global.dto;


public class HospitalAccountMasterDto {
	
	private Integer hospitalAccountId;
	
	private String hospitalAccountCode;
	
	private String hospitalAccountDesc;

	private Character isDefault;

	private Integer orgnizationId;
	
	private Integer unitId;

	private Character status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;

	public Integer getHospitalAccountId() {
		return hospitalAccountId;
	}

	public String getHospitalAccountCode() {
		return hospitalAccountCode;
	}

	public String getHospitalAccountDesc() {
		return hospitalAccountDesc;
	}

	public Character getIsDefault() {
		return isDefault;
	}

	public Integer getOrgnizationId() {
		return orgnizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public Character getStatus() {
		return status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setHospitalAccountId(Integer hospitalAccountId) {
		this.hospitalAccountId = hospitalAccountId;
	}

	public void setHospitalAccountCode(String hospitalAccountCode) {
		this.hospitalAccountCode = hospitalAccountCode;
	}

	public void setHospitalAccountDesc(String hospitalAccountDesc) {
		this.hospitalAccountDesc = hospitalAccountDesc;
	}

	public void setIsDefault(Character isDefault) {
		this.isDefault = isDefault;
	}

	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getOffset() {
		return offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}
	
	
}
