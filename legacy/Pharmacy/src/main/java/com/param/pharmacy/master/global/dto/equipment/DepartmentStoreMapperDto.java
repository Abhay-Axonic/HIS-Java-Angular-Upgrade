package com.param.pharmacy.master.global.dto.equipment;

import java.util.Date;
import java.util.List;

public class DepartmentStoreMapperDto {
   
	private Integer deptStoreMapperId;
	
	private Integer storeId;
	
	private Integer deptId;
	
	private Integer createdBy;
	
	private Date createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private Date updatedDate;
	
	private Integer unitId;

	private Integer orgId;
	
	private String deptName;
	
	private String storeName;
	
	private Integer id;
	
	 private Integer offset;
		
		private Integer noOfRecordsPerPage;
	
	/* start code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */
	//use for request getDeptStoremappedListNew
	private List<Integer> storeIds;	
	private List<Integer> deptIds;
	
	public List<Integer> getStoreIds() {
		return storeIds;
	}
	public void setStoreIds(List<Integer> storeIds) {
		this.storeIds = storeIds;
	}
	public List<Integer> getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(List<Integer> deptIds) {
		this.deptIds = deptIds;
	}
	/* end code for resolved HIS-3198 Department Store mapper - User should be able to assign the one store to multiple department and vice versa */

	public Integer getDeptStoreMapperId() {
		return deptStoreMapperId;
	}

	public void setDeptStoreMapperId(Integer deptStoreMapperId) {
		this.deptStoreMapperId = deptStoreMapperId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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
		return "DepartmentStoreMapperDto [deptStoreMapperId=" + deptStoreMapperId + ", storeId=" + storeId + ", deptId="
				+ deptId + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", status=" + status
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", unitId=" + unitId + ", orgId="
				+ orgId + ", deptName=" + deptName + ", storeName=" + storeName + ", getDeptStoreMapperId()="
				+ getDeptStoreMapperId() + ", getStoreId()=" + getStoreId() + ", getDeptId()=" + getDeptId()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getCreatedDate()=" + getCreatedDate() + ", getStatus()="
				+ getStatus() + ", getUpdatedBy()=" + getUpdatedBy() + ", getUpdatedDate()=" + getUpdatedDate()
				+ ", getUnitId()=" + getUnitId() + ", getOrgId()=" + getOrgId() + ", getDeptName()=" + getDeptName()
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
