package com.param.bloodBank.dto;

import java.util.Date;

public class BloodBankStockDetailsDto {
	
	private Integer bloodBankStockDetailsId;
	
	private Integer bloodBankId;
	
	private Integer bloodGroupId;
	
	private Integer bloodBagCapacityId;
	
	private Integer bloodBagTypeId;
	
	private Integer bloodComponentId ;
	
	private Integer availableStock ;
	
	private Integer createdBy;
	
	private Date createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private Date updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private String bloodBagCapacityName;
	
	private String bloodBagTypeName;
	
	private String bloodGroupName;
	
	private String bloodBankName;
	
	public String getBloodBagCapacityName() {
		return bloodBagCapacityName;
	}

	public void setBloodBagCapacityName(String bloodBagCapacityName) {
		this.bloodBagCapacityName = bloodBagCapacityName;
	}

	public String getBloodBagTypeName() {
		return bloodBagTypeName;
	}

	public void setBloodBagTypeName(String bloodBagTypeName) {
		this.bloodBagTypeName = bloodBagTypeName;
	}

	public String getBloodGroupName() {
		return bloodGroupName;
	}

	public void setBloodGroupName(String bloodGroupName) {
		this.bloodGroupName = bloodGroupName;
	}

	public String getBloodBankName() {
		return bloodBankName;
	}

	public void setBloodBankName(String bloodBankName) {
		this.bloodBankName = bloodBankName;
	}

	public Integer getBloodBankStockDetailsId() {
		return bloodBankStockDetailsId;
	}

	public void setBloodBankStockDetailsId(Integer bloodBankStockDetailsId) {
		this.bloodBankStockDetailsId = bloodBankStockDetailsId;
	}

	public Integer getBloodBankId() {
		return bloodBankId;
	}

	public void setBloodBankId(Integer bloodBankId) {
		this.bloodBankId = bloodBankId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Integer getBloodBagCapacityId() {
		return bloodBagCapacityId;
	}

	public void setBloodBagCapacityId(Integer bloodBagCapacityId) {
		this.bloodBagCapacityId = bloodBagCapacityId;
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

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
	
	
}
