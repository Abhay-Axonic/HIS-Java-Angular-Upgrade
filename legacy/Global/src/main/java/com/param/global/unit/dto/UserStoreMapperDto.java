package com.param.global.unit.dto;

import java.util.List;


public class UserStoreMapperDto {
	
	private Integer userStoreMapperId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer userId;
	
	private Integer storeId;
	
	private Boolean isActive;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String empCode;

	private String employeeName;

	private Integer addedBy;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;

	private String addedDate;
	
	private String storeName;
	
	private Boolean isDeleted;
	
	private  List <Integer> selectUserStoreMapperList;

	public Integer getUserStoreMapperId() {
		return userStoreMapperId;
	}

	public void setUserStoreMapperId(Integer userStoreMapperId) {
		this.userStoreMapperId = userStoreMapperId;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<Integer> getSelectUserStoreMapperList() {
		return selectUserStoreMapperList;
	}

	public void setSelectUserStoreMapperList(List<Integer> selectUserStoreMapperList) {
		this.selectUserStoreMapperList = selectUserStoreMapperList;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	
}
