package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_company_receipt_master" , schema = "billing")
@EntityListeners(value = CompanyReceiptMaster.class)
@SequenceGenerator(name = "m_company_receipt_master_seq" , sequenceName = "billing.m_company_receipt_master_seq" , allocationSize = 1)
public class CompanyReceiptMaster {
	
	@Id
	@Column(name = "company_receipt_id", unique = true , nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_company_receipt_master_seq")
	private int companyReceiptId;
	
	@Column(name = "company_receipt_number")
	private String companyReceiptNumber;
	
	@Column(name = "company_receipt_date")
	private Date companyReceiptDate;
	
	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name = "payee_id")
	private Integer payeeId;
	
	@Column(name = "receipt_id")
	private Integer receiptId;
	
	@Column(name = "receipt_amount")
	private double receiptAmount;
	
	@Column(name = "tds_amount")
	private double tdsAmount;
	
	@Column(name = "other_deduction")
	private double otherDeduction;
	
	@Column(name = "disallowed_amount")
	private double disallowedAmount;
	
	@Column(name = "net_receipt_amount")
	private double netReceiptAmount;
	
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
	
	@Column(name = "deposit_consumption_id")
    private Integer depositConsumptionId;
    

    public Integer getDepositConsumptionId() {
        return depositConsumptionId;
    }

    public void setDepositConsumptionId(Integer depositConsumptionId) {
        this.depositConsumptionId = depositConsumptionId;
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

	public Date getCompanyReceiptDate() {
		return companyReceiptDate;
	}

	public void setCompanyReceiptDate(Date companyReceiptDate) {
		this.companyReceiptDate = companyReceiptDate;
	}
}
