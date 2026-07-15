package com.param.module.service;

import java.util.List;

import com.param.global.otims.dto.BatchDto;

public class ServiceItemMapperDto {

	private Integer serviceItemMapperId;

	private Integer itemId;
	
	private Integer assignQty;
	
	private Integer serviceId;
	
	private Integer organizationId;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private char status;
	
	private String itemName;
	
	private Integer unitId;
	
	private String itemCode;
	
	private List<BatchDto> batchList;
	
	private List<ServiceItemMapperDto> itemIds;
	
	private List<Integer> serviceIds;
	
	private Boolean batchReq;
	
	private Integer storeId;

	public Integer getServiceItemMapperId() {
		return serviceItemMapperId;
	}

	public void setServiceItemMapperId(Integer serviceItemMapperId) {
		this.serviceItemMapperId = serviceItemMapperId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAssignQty() {
		return assignQty;
	}

	public void setAssignQty(Integer assignQty) {
		this.assignQty = assignQty;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public List<BatchDto> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<BatchDto> batchList) {
		this.batchList = batchList;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public List<ServiceItemMapperDto> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<ServiceItemMapperDto> itemIds) {
		this.itemIds = itemIds;
	}

	public List<Integer> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(List<Integer> serviceIds) {
		this.serviceIds = serviceIds;
	}

	public Boolean getBatchReq() {
		return batchReq;
	}

	public void setBatchReq(Boolean batchReq) {
		this.batchReq = batchReq;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	
}
