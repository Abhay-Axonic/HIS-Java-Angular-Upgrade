package com.param.billing.cashDesk.dto;

import java.util.List;

import com.param.global.dto.DenominationDetailsMasterDto;

public class CashHandoverDetailsDto {
	
	private Integer id;
	
	private String date;
	
	private String cobNumber;
	
	private Integer ichNumber;
	
	private Integer cashHandoverById;
	
	private Integer cashHandoverToId;
	
	private Double cashHandoverAmount;
	
	private Integer denominationId;
	
	private Integer quantity;
	
	private Integer technicianId;
	
	private String remarks;
	
	private Integer organizationId;
	
	private Integer unitId;

	private String handoverByPassword;
	
	private String handoverToPassword;
	
	private Integer userId;

	private String createdDate;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String status;
	
	private String fromDate;
	
	private String toDate;
	
	private String handoverByUser;
	
	private String handoverToUser;
	
	private List<DenominationDetailsMasterDto> denominationDetailsMasterList;
	
	private boolean check;
	
	private Integer statusId;
	
	private boolean shiftClose;
	
	private Integer cashTransactionTypeId;
	
	private Integer hospitalAccountId;
	
	private String hospitalAccount;
	
	private Integer sr;
	
	private String lebel;
	
	private Double debitAmount;
	
	private Double creditAmount;
	
	
	public String getCobNumber() {
		return cobNumber;
	}

	public void setCobNumber(String cobNumber) {
		this.cobNumber = cobNumber;
	}

	public Integer getIchNumber() {
		return ichNumber;
	}

	public void setIchNumber(Integer ichNumber) {
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

	public Integer getDenominationId() {
		return denominationId;
	}

	public void setDenominationId(Integer denominationId) {
		this.denominationId = denominationId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public boolean isShiftClose() {
		return shiftClose;
	}

	public void setShiftClose(boolean shiftClose) {
		this.shiftClose = shiftClose;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getCashTransactionTypeId() {
		return cashTransactionTypeId;
	}

	public void setCashTransactionTypeId(Integer cashTransactionTypeId) {
		this.cashTransactionTypeId = cashTransactionTypeId;
	}

	public Integer getHospitalAccountId() {
		return hospitalAccountId;
	}

	public void setHospitalAccountId(Integer hospitalAccountId) {
		this.hospitalAccountId = hospitalAccountId;
	}

	public String getHospitalAccount() {
		return hospitalAccount;
	}

	public void setHospitalAccount(String hospitalAccount) {
		this.hospitalAccount = hospitalAccount;
	}

	public Integer getSr() {
		return sr;
	}

	public void setSr(Integer sr) {
		this.sr = sr;
	}

	public String getLebel() {
		return lebel;
	}

	public void setLebel(String lebel) {
		this.lebel = lebel;
	}

	public Double getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}

	public Double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	

}
