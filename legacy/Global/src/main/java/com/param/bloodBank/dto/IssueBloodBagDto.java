package com.param.bloodBank.dto;

import java.util.List;
public class IssueBloodBagDto {

	private Integer issueBloodBagId;
	
	private Integer patientId;
	
	private Integer crossMatchRegisterId;

	private Integer btoId;
	
	private Integer issuerId;
	
	private Integer receivedId;
	
	private Integer bloodUnitQty;
	
	private String requisitionDate;
	
	private String issueDate;
	
	private Integer issueStatusId;
	
	private boolean isExported;
	
	private String remarks;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private String bloodBagIssueNo;

	private List<IssueBloodBagDetailMapperDto> bloodBagIssueList;
	
	public Integer getIssueBloodBagId() {
		return issueBloodBagId;
	}

	public void setIssueBloodBagId(Integer issueBloodBagId) {
		this.issueBloodBagId = issueBloodBagId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	
	public Integer getCrossMatchRegisterId() {
		return crossMatchRegisterId;
	}

	public void setCrossMatchRegisterId(Integer crossMatchRegisterId) {
		this.crossMatchRegisterId = crossMatchRegisterId;
	}

	public Integer getBtoId() {
		return btoId;
	}

	public void setBtoId(Integer btoId) {
		this.btoId = btoId;
	}

	public Integer getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(Integer issuerId) {
		this.issuerId = issuerId;
	}

	public Integer getReceivedId() {
		return receivedId;
	}

	public void setReceivedId(Integer receivedId) {
		this.receivedId = receivedId;
	}

	public Integer getBloodUnitQty() {
		return bloodUnitQty;
	}

	public void setBloodUnitQty(Integer bloodUnitQty) {
		this.bloodUnitQty = bloodUnitQty;
	}

	public String getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(String requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getIssueStatusId() {
		return issueStatusId;
	}

	public void setIssueStatusId(Integer issueStatusId) {
		this.issueStatusId = issueStatusId;
	}

	public boolean isExported() {
		return isExported;
	}

	public void setExported(boolean isExported) {
		this.isExported = isExported;
	}

	public List<IssueBloodBagDetailMapperDto> getBloodBagIssueList() {
		return bloodBagIssueList;
	}

	public void setBloodBagIssueList(List<IssueBloodBagDetailMapperDto> bloodBagIssueList) {
		this.bloodBagIssueList = bloodBagIssueList;
	}

	public String getBloodBagIssueNo() {
		return bloodBagIssueNo;
	}

	public void setBloodBagIssueNo(String bloodBagIssueNo) {
		this.bloodBagIssueNo = bloodBagIssueNo;
	}
	
}
