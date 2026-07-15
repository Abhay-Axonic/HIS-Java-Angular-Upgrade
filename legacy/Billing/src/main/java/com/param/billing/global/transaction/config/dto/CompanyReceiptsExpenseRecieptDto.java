package com.param.billing.global.transaction.config.dto;

import java.util.List;

public class CompanyReceiptsExpenseRecieptDto {


	private Integer companyReceiptsExpenseRecieptId;
	
	private Integer companyReceiptsExpenseId;
	
	private String recieptNumber;

	private String dateOfReciept;

	private Double amount;
	
	private Character recieptCancelled;
	
	private Integer recieptCancelReasonId;

	private String recieptCancelDatetime;

	private Integer recieptCancelBy;

	private String recieptCancelRemark;
	
	private Integer unitId;

	private Integer orgId;

	private Character status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private List<CompanyRecieptsReceiptDetailsDto> companyRecieptsReceiptDetailsDtoList;
	
	

	public List<CompanyRecieptsReceiptDetailsDto> getCompanyRecieptsReceiptDetailsDtoList() {
		return companyRecieptsReceiptDetailsDtoList;
	}

	public void setCompanyRecieptsReceiptDetailsDtoList(
			List<CompanyRecieptsReceiptDetailsDto> companyRecieptsReceiptDetailsDtoList) {
		this.companyRecieptsReceiptDetailsDtoList = companyRecieptsReceiptDetailsDtoList;
	}

	public Integer getCompanyReceiptsExpenseRecieptId() {
		return companyReceiptsExpenseRecieptId;
	}

	public void setCompanyReceiptsExpenseRecieptId(Integer companyReceiptsExpenseRecieptId) {
		this.companyReceiptsExpenseRecieptId = companyReceiptsExpenseRecieptId;
	}

	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}

	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
	}

	public String getRecieptNumber() {
		return recieptNumber;
	}

	public void setRecieptNumber(String recieptNumber) {
		this.recieptNumber = recieptNumber;
	}

	public String getDateOfReciept() {
		return dateOfReciept;
	}

	public void setDateOfReciept(String dateOfReciept) {
		this.dateOfReciept = dateOfReciept;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Character getRecieptCancelled() {
		return recieptCancelled;
	}

	public void setRecieptCancelled(Character recieptCancelled) {
		this.recieptCancelled = recieptCancelled;
	}

	public Integer getRecieptCancelReasonId() {
		return recieptCancelReasonId;
	}

	public void setRecieptCancelReasonId(Integer recieptCancelReasonId) {
		this.recieptCancelReasonId = recieptCancelReasonId;
	}

	public String getRecieptCancelDatetime() {
		return recieptCancelDatetime;
	}

	public void setRecieptCancelDatetime(String recieptCancelDatetime) {
		this.recieptCancelDatetime = recieptCancelDatetime;
	}

	public Integer getRecieptCancelBy() {
		return recieptCancelBy;
	}

	public void setRecieptCancelBy(Integer recieptCancelBy) {
		this.recieptCancelBy = recieptCancelBy;
	}

	public String getRecieptCancelRemark() {
		return recieptCancelRemark;
	}

	public void setRecieptCancelRemark(String recieptCancelRemark) {
		this.recieptCancelRemark = recieptCancelRemark;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
	
}
