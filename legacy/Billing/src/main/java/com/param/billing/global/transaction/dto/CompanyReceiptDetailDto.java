package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;

public class CompanyReceiptDetailDto {
	private int companyReceiptDetailId;
	private Integer companyReceiptId;
	private BigDecimal netReceiptAmount;
	private BigDecimal tdsAmount;
	private BigDecimal otherDeduction;
	private BigDecimal disallowedAmount;
	private Integer companyInvoiceDetailId;
	private BigDecimal totalAmount;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private Integer orgId;
	private Integer unitId;
	private Integer billId;
	private Integer saleId;
	private Integer payeeId;
	private Integer associateCompanyId;
	private BigDecimal outstandingAmt;
	private List<TCompanyInvoiceServiceDetailsDto> serviceDetilsList;
	
	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}
	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public List<TCompanyInvoiceServiceDetailsDto> getServiceDetilsList() {
		return serviceDetilsList;
	}
	public void setServiceDetilsList(List<TCompanyInvoiceServiceDetailsDto> serviceDetilsList) {
		this.serviceDetilsList = serviceDetilsList;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getOutstandingAmt() {
		return outstandingAmt;
	}
	public void setOutstandingAmt(BigDecimal outstandingAmt) {
		this.outstandingAmt = outstandingAmt;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public int getCompanyReceiptDetailId() {
		return companyReceiptDetailId;
	}
	public void setCompanyReceiptDetailId(int companyReceiptDetailId) {
		this.companyReceiptDetailId = companyReceiptDetailId;
	}
	public Integer getCompanyReceiptId() {
		return companyReceiptId;
	}
	public void setCompanyReceiptId(Integer companyReceiptId) {
		this.companyReceiptId = companyReceiptId;
	}
	public Integer getCompanyInvoiceDetailId() {
		return companyInvoiceDetailId;
	}
	public void setCompanyInvoiceDetailId(Integer companyInvoiceDetailId) {
		this.companyInvoiceDetailId = companyInvoiceDetailId;
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
	public BigDecimal getNetReceiptAmount() {
		return netReceiptAmount;
	}
	public void setNetReceiptAmount(BigDecimal netReceiptAmount) {
		this.netReceiptAmount = netReceiptAmount;
	}
	public BigDecimal getTdsAmount() {
		return tdsAmount;
	}
	public void setTdsAmount(BigDecimal tdsAmount) {
		this.tdsAmount = tdsAmount;
	}
	public BigDecimal getOtherDeduction() {
		return otherDeduction;
	}
	public void setOtherDeduction(BigDecimal otherDeduction) {
		this.otherDeduction = otherDeduction;
	}
	public BigDecimal getDisallowedAmount() {
		return disallowedAmount;
	}
	public void setDisallowedAmount(BigDecimal disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}
	public Integer getSaleId() {
		return saleId;
	}
	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}
	
	
}