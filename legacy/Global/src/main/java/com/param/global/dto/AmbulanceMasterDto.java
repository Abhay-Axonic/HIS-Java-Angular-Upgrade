package com.param.global.dto;

public class AmbulanceMasterDto {

	private int ambulanceMasterId;
	
	private String ambulanceType;
	
	private String serviceProvider;
	
	private String ambulanceVehicalNo;
	
	private String availableFacilities;
	
	private char inhouse;
	
	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	public String getAmbulanceType() {
		return ambulanceType;
	}

	public void setAmbulanceType(String ambulanceType) {
		this.ambulanceType = ambulanceType;
	}

	public int getAmbulanceMasterId() {
		return ambulanceMasterId;
	}

	public void setAmbulanceMasterId(int ambulanceMasterId) {
		this.ambulanceMasterId = ambulanceMasterId;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getAmbulanceVehicalNo() {
		return ambulanceVehicalNo;
	}

	public void setAmbulanceVehicalNo(String ambulanceVehicalNo) {
		this.ambulanceVehicalNo = ambulanceVehicalNo;
	}

	public String getAvailableFacilities() {
		return availableFacilities;
	}

	public void setAvailableFacilities(String availableFacilities) {
		this.availableFacilities = availableFacilities;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
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

	public char getInhouse() {
		return inhouse;
	}

	public void setInhouse(char inhouse) {
		this.inhouse = inhouse;
	}


}
