package com.param.global.otims.dto;

import java.util.List;

public class OperationTheatreDto {
	
	private Integer operationTheatreId;

	private String theatreCode;

	private String theatreName;

	private String theatreDesc;

	private Integer theatreTypeId;
	
	private String theatreTypeName;

	private Character status;

	private Integer orgId;

	private Integer unitId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private Integer storeId;
	
	private String storeName;

	private List<OperationTableTypeDto> OperationTableList;
	
	
	public Integer getOperationTheatreId() {
		return operationTheatreId;
	}

	public void setOperationTheatreId(Integer operationTheatreId) {
		this.operationTheatreId = operationTheatreId;
	}

	public String getTheatreCode() {
		return theatreCode;
	}

	public void setTheatreCode(String theatreCode) {
		this.theatreCode = theatreCode;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getTheatreDesc() {
		return theatreDesc;
	}

	public void setTheatreDesc(String theatreDesc) {
		this.theatreDesc = theatreDesc;
	}

	public Integer getTheatreTypeId() {
		return theatreTypeId;
	}

	public void setTheatreTypeId(Integer theatreTypeId) {
		this.theatreTypeId = theatreTypeId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public String getTheatreTypeName() {
		return theatreTypeName;
	}

	public void setTheatreTypeName(String theatreTypeName) {
		this.theatreTypeName = theatreTypeName;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public List<OperationTableTypeDto> getOperationTableList() {
		return OperationTableList;
	}

	public void setOperationTableList(List<OperationTableTypeDto> operationTableList) {
		OperationTableList = operationTableList;
	}
	
	
}
