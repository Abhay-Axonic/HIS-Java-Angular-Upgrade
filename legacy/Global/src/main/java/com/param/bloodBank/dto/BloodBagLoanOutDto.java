package com.param.bloodBank.dto;

import java.util.List;

public class BloodBagLoanOutDto {

	private Integer bloodBagLoanOutId;
	
	private String loanOutNumber;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private String userName;
	
	private String fromDate;
	
	private String toDate;

	private List<BloodBagLoanOutDetailsDto> bloodBagLoanOutDetailsList;
	
	private Integer approvedBy;
	
	private Integer loanOutStatusId;

 
	public Integer getBloodBagLoanOutId() {
		return bloodBagLoanOutId;
	}

	public void setBloodBagLoanOutId(Integer bloodBagLoanOutId) {
		this.bloodBagLoanOutId = bloodBagLoanOutId;
	}

	public String getLoanOutNumber() {
		return loanOutNumber;
	}

	public void setLoanOutNumber(String loanOutNumber) {
		this.loanOutNumber = loanOutNumber;
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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
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

	public List<BloodBagLoanOutDetailsDto> getBloodBagLoanOutDetailsList() {
		return bloodBagLoanOutDetailsList;
	}

	public void setBloodBagLoanOutDetailsList(List<BloodBagLoanOutDetailsDto> bloodBagLoanOutDetailsList) {
		this.bloodBagLoanOutDetailsList = bloodBagLoanOutDetailsList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Integer getLoanOutStatusId() {
		return loanOutStatusId;
	}

	public void setLoanOutStatusId(Integer loanOutStatusId) {
		this.loanOutStatusId = loanOutStatusId;
	}
}
