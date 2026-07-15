package com.param.procedure.dto;

import java.util.List;

public class OTCompletionCssdKitDto {
	
	private Integer otCompletionCssdKitId;
	
	private Integer kitTransitionNo;
	
	private Integer proReqId;
	
	private Integer kitId;
	
	private Integer batchId;
	
	private String kitName;
	
	private String kitCode;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer storeId;
	
	private List<CssdKitVO> batchList;
	
	private Integer kitTransitionId;
	
	private String kitExpiryDate;
	
	private String batchNumber;
	
	private String searchKeyword;
	

	public Integer getOtCompletionCssdKitId() {
		return otCompletionCssdKitId;
	}

	public void setOtCompletionCssdKitId(Integer otCompletionCssdKitId) {
		this.otCompletionCssdKitId = otCompletionCssdKitId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getKitName() {
		return kitName;
	}

	public void setKitName(String kitName) {
		this.kitName = kitName;
	}

	public String getKitCode() {
		return kitCode;
	}

	public void setKitCode(String kitCode) {
		this.kitCode = kitCode;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
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

	public Integer getKitTransitionNo() {
		return kitTransitionNo;
	}

	public void setKitTransitionNo(Integer kitTransitionNo) {
		this.kitTransitionNo = kitTransitionNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}


	public Integer getKitTransitionId() {
		return kitTransitionId;
	}

	public void setKitTransitionId(Integer kitTransitionId) {
		this.kitTransitionId = kitTransitionId;
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

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public List<CssdKitVO> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<CssdKitVO> batchList) {
		this.batchList = batchList;
	}

}
