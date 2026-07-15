package com.param.pharmacy.master.global.dto.equipment;

import java.util.Date;

public class WardStoreMapperDto {

	   
	private Integer wardStoreMapperId;
	
	private Integer storeId;
	
	private Integer wardId;
	
	private Integer createdBy;
	
	private Date createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private Date updatedDate;
	
	private Integer unitId;

	private Integer orgId;
	
	private String wardName;
	
	private String storeName;
	
	private Integer id;
	
	 private Integer offset;
		
		private Integer noOfRecordsPerPage;
	

	public Integer getwardStoreMapperId() {
		return wardStoreMapperId;
	}

	public void setwardStoreMapperId(Integer wardStoreMapperId) {
		this.wardStoreMapperId = wardStoreMapperId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getwardId() {
		return wardId;
	}

	public void setwardId(Integer wardId) {
		this.wardId = wardId;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getwardName() {
		return wardName;
	}

	public void setwardName(String wardName) {
		this.wardName = wardName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	@Override
	public String toString() {
		return "DepartmentStoreMapperDto [wardStoreMapperId=" + wardStoreMapperId + ", storeId=" + storeId + ", wardId="
				+ wardId + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", status=" + status
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", unitId=" + unitId + ", orgId="
				+ orgId + ", wardName=" + wardName + ", storeName=" + storeName + ", getwardStoreMapperId()="
				+ getwardStoreMapperId() + ", getStoreId()=" + getStoreId() + ", getwardId()=" + getwardId()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getCreatedDate()=" + getCreatedDate() + ", getStatus()="
				+ getStatus() + ", getUpdatedBy()=" + getUpdatedBy() + ", getUpdatedDate()=" + getUpdatedDate()
				+ ", getUnitId()=" + getUnitId() + ", getOrgId()=" + getOrgId() + ", getwardName()=" + getwardName()
				+ ", getStoreName()=" + getStoreName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
