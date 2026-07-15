package com.param.bloodBank.dto;

import java.util.List;


public class BloodBankTypeComponentMasterDto {
	private Integer bloodBankTypeComponentId;
	
	private Integer bloodBagTypeId;
	
	private Integer bloodComponentId;
	
	private Character status;
	
	private Character byCrossChecking;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Character isDeleted;
	
	private Character noOfRecordsPerPage;

	private Character offset;
	
	private String bloodComponentDesc;
	
	private String bloodComponentCode;
	
	private String bloodComponentShortDesc;

	private Integer expiry;

	private Integer quantity;
	
	private String bloodBagTypeDesc;



	
	private List<Integer> selectBloodBankTypeComponentMasterList;

	public Integer getBloodBankTypeComponentId() {
		return bloodBankTypeComponentId;
	}

	public void setBloodBankTypeComponentId(Integer bloodBankTypeComponentId) {
		this.bloodBankTypeComponentId = bloodBankTypeComponentId;
	}

	public Integer getBloodBagTypeId() {
		return bloodBagTypeId;
	}

	public void setBloodBagTypeId(Integer bloodBagTypeId) {
		this.bloodBagTypeId = bloodBagTypeId;
	}

	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Character getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Character noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Character getOffset() {
		return offset;
	}

	public void setOffset(Character offset) {
		this.offset = offset;
	}

	public List<Integer> getSelectBloodBankTypeComponentMasterList() {
		return selectBloodBankTypeComponentMasterList;
	}

	public void setSelectBloodBankTypeComponentMasterList(List<Integer> selectBloodBankTypeComponentMasterList) {
		this.selectBloodBankTypeComponentMasterList = selectBloodBankTypeComponentMasterList;
	}

	public String getBloodComponentDesc() {
		return bloodComponentDesc;
	}

	public void setBloodComponentDesc(String bloodComponentDesc) {
		this.bloodComponentDesc = bloodComponentDesc;
	}

	public String getBloodComponentCode() {
		return bloodComponentCode;
	}

	public void setBloodComponentCode(String bloodComponentCode) {
		this.bloodComponentCode = bloodComponentCode;
	}

	public String getBloodComponentShortDesc() {
		return bloodComponentShortDesc;
	}

	public void setBloodComponentShortDesc(String bloodComponentShortDesc) {
		this.bloodComponentShortDesc = bloodComponentShortDesc;
	}

	public Integer getExpiry() {
		return expiry;
	}

	public void setExpiry(Integer expiry) {
		this.expiry = expiry;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getByCrossChecking() {
		return byCrossChecking;
	}

	public void setByCrossChecking(Character byCrossChecking) {
		this.byCrossChecking = byCrossChecking;
	}

	public String getBloodBagTypeDesc() {
		return bloodBagTypeDesc;
	}

	public void setBloodBagTypeDesc(String bloodBagTypeDesc) {
		this.bloodBagTypeDesc = bloodBagTypeDesc;
	}
	

}
