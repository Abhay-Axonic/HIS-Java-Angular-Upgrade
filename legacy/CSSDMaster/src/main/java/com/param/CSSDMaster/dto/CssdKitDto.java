package com.param.CSSDMaster.dto;

import java.util.List;

public class CssdKitDto {
	
	private Integer kitId;
	private String kitCode;
	private String kitName;
	private Integer storeId;
	private Integer organizationId;
	private Integer unitId;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String updatedDate;
	private Character status;
	private Integer kitTransitionId;
	private String kitExpiryDate;
	private String batchNumber;
	private String searchKeyword;
	
	private List<CssdKitDto> batchList;
	
	public Integer getKitId() {
		return kitId;
	}
	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}
	public String getKitCode() {
		return kitCode;
	}
	public void setKitCode(String kitCode) {
		this.kitCode = kitCode;
	}
	public String getKitName() {
		return kitName;
	}
	public void setKitName(String kitName) {
		this.kitName = kitName;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public String getKitExpiryDate() {
		return kitExpiryDate;
	}
	public void setKitExpiryDate(String kitExpiryDate) {
		this.kitExpiryDate = kitExpiryDate;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public Integer getKitTransitionNo() {
		return kitTransitionId;
	}
	public void setKitTransitionNo(Integer kitTransitionId) {
		this.kitTransitionId = kitTransitionId;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public Integer getKitTransitionId() {
		return kitTransitionId;
	}
	public void setKitTransitionId(Integer kitTransitionId) {
		this.kitTransitionId = kitTransitionId;
	}
	public List<CssdKitDto> getBatchList() {
		return batchList;
	}
	public void setBatchList(List<CssdKitDto> batchList) {
		this.batchList = batchList;
	}
}
