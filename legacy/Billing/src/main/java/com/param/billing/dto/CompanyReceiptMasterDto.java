package com.param.billing.dto;

import java.util.List;

import com.param.billing.global.transaction.dto.CompanyReceiptDetailDto;

public class CompanyReceiptMasterDto {
	private int companyReceiptId;
	private String companyReceiptNumber;
	private Integer paymentEntitlementId;
	private Integer payeeId;
	private Integer associateCompanyId;
	private Integer receiptId;
	private double receiptAmount;
	private double tdsAmount;
	private double otherDeduction;
	private double disallowedAmount;
	private double netReceiptAmount;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private Integer orgId;
	private Integer unitId;
	private List<CompanyReceiptDetailDto> receiptDetails;
	private Integer depositConsumptionId;

	public Integer getDepositConsumptionId() {
		return depositConsumptionId;
	}

	public void setDepositConsumptionId(Integer depositConsumptionId) {
		this.depositConsumptionId = depositConsumptionId;
	}
	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}
	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}
	public List<CompanyReceiptDetailDto> getReceiptDetails() {
		return receiptDetails;
	}
	public void setReceiptDetails(List<CompanyReceiptDetailDto> receiptDetails) {
		this.receiptDetails = receiptDetails;
	}
	public int getCompanyReceiptId() {
		return companyReceiptId;
	}
	public void setCompanyReceiptId(int companyReceiptId) {
		this.companyReceiptId = companyReceiptId;
	}
	public String getCompanyReceiptNumber() {
		return companyReceiptNumber;
	}
	public void setCompanyReceiptNumber(String companyReceiptNumber) {
		this.companyReceiptNumber = companyReceiptNumber;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public Integer getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}
	public double getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public double getTdsAmount() {
		return tdsAmount;
	}
	public void setTdsAmount(double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}
	public double getOtherDeduction() {
		return otherDeduction;
	}
	public void setOtherDeduction(double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}
	public double getDisallowedAmount() {
		return disallowedAmount;
	}
	public void setDisallowedAmount(double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}
	public double getNetReceiptAmount() {
		return netReceiptAmount;
	}
	public void setNetReceiptAmount(double netReceiptAmount) {
		this.netReceiptAmount = netReceiptAmount;
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
	
}
