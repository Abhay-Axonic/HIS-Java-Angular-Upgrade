package com.param.billing.cashDesk.dto;

import java.util.List;

import com.param.global.dto.DenominationDetailsMasterDto;

public class InterimHandoverMasterDto {

	private Integer id;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String ichNumber;
	
	private Integer cashHandoverById;
	
	private Integer cashHandoverToId;
	
	private Double cashHandoverAmount;
	
	private String remarks;
	
	private String updatedDate;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String status;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private String fromDate;
	
	private String toDate;
	
	private String handoverByPassword;
	
	private String handoverToPassword;
	
	private Integer userId;
	
	private String password;

	private String modeOfTransaction;
	
	private String currentDate;
	
	private List<BillingIdDetailsDto> listOfIds;
	
	private String handoverByUser;
	
	private String handoverToUser;
	
	private Integer statusId;
	
	private List<DenominationDetailsMasterDto> denominationDetailsMasterList;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getIchNumber() {
		return ichNumber;
	}

	public void setIchNumber(String ichNumber) {
		this.ichNumber = ichNumber;
	}

	public Integer getCashHandoverById() {
		return cashHandoverById;
	}

	public void setCashHandoverById(Integer cashHandoverById) {
		this.cashHandoverById = cashHandoverById;
	}

	public Integer getCashHandoverToId() {
		return cashHandoverToId;
	}

	public void setCashHandoverToId(Integer cashHandoverToId) {
		this.cashHandoverToId = cashHandoverToId;
	}
	
	public Double getCashHandoverAmount() {
		return cashHandoverAmount;
	}

	public void setCashHandoverAmount(Double cashHandoverAmount) {
		this.cashHandoverAmount = cashHandoverAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getHandoverByPassword() {
		return handoverByPassword;
	}

	public void setHandoverByPassword(String handoverByPassword) {
		this.handoverByPassword = handoverByPassword;
	}

	public String getHandoverToPassword() {
		return handoverToPassword;
	}

	public void setHandoverToPassword(String handoverToPassword) {
		this.handoverToPassword = handoverToPassword;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getModeOfTransaction() {
		return modeOfTransaction;
	}

	public void setModeOfTransaction(String modeOfTransaction) {
		this.modeOfTransaction = modeOfTransaction;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public List<BillingIdDetailsDto> getListOfIds() {
		return listOfIds;
	}

	public void setListOfIds(List<BillingIdDetailsDto> listOfIds) {
		this.listOfIds = listOfIds;
	}

	public String getHandoverByUser() {
		return handoverByUser;
	}

	public void setHandoverByUser(String handoverByUser) {
		this.handoverByUser = handoverByUser;
	}

	public String getHandoverToUser() {
		return handoverToUser;
	}

	public void setHandoverToUser(String handoverToUser) {
		this.handoverToUser = handoverToUser;
	}

	public List<DenominationDetailsMasterDto> getDenominationDetailsMasterList() {
		return denominationDetailsMasterList;
	}

	public void setDenominationDetailsMasterList(List<DenominationDetailsMasterDto> denominationDetailsMasterList) {
		this.denominationDetailsMasterList = denominationDetailsMasterList;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	
	
}
