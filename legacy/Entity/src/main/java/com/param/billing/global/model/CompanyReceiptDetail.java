package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.utility.TimeZoneComponent;

@Entity
@Table(name = "t_company_receipt_detail" , schema = "billing")
@SequenceGenerator(name = "t_company_receipt_detail_seq" , sequenceName = "billing.t_company_receipt_detail_seq" , allocationSize = 1)
public class CompanyReceiptDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_company_receipt_detail_seq")
	@Column(name = "company_receipt_detail_id" , unique = true , nullable = false)
	private int companyReceiptDetailId;
	
	@Column(name = "company_receipt_id")
	private Integer companyReceiptId;
	
	@Column(name = "net_receipt_amount")
	private double netReceiptAmount;
	
	@Column(name = "tds_amount")
	private double tdsAmount;
	
	@Column(name = "other_deduction")
	private double otherDeduction;
	
	@Column(name = "disallowed_amount")
	private double disallowedAmount;
	
	@Column(name = "company_invoice_detail_id")
	private Integer companyInvoiceDetailId;
	
	@Column(name = "total_amount")
	private double totalAmount;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "organisation_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
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

	public double getNetReceiptAmount() {
		return netReceiptAmount;
	}

	public void setNetReceiptAmount(double netReceiptAmount) {
		this.netReceiptAmount = netReceiptAmount;
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

	public Integer getCompanyInvoiceDetailId() {
		return companyInvoiceDetailId;
	}

	public void setCompanyInvoiceDetailId(Integer companyInvoiceDetailId) {
		this.companyInvoiceDetailId = companyInvoiceDetailId;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
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
	
	@PrePersist
	private void prePersist() {
		setCreatedDate(TimeZoneComponent.getDateByzone(0));;
	}
	
	@PreUpdate
	private void preUpdate(){
		setUpdatedDate(TimeZoneComponent.getDateByzone(0));
	}
}
