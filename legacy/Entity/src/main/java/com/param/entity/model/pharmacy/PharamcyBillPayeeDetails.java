package com.param.entity.model.pharmacy;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@Entity(name = "PharamcyBillPayeeDetails")
@Table(name = "t_pharmacy_bill_payee_details", schema = "pharmacy")
@SequenceGenerator(name = "t_pharmacy_bill_payee_details_seq", sequenceName = "pharmacy.t_pharmacy_bill_payee_details_seq", allocationSize = 1)
public class PharamcyBillPayeeDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 367157723144475862L;

	@Id
	@Column(name = "payee_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_pharmacy_bill_payee_details_seq")
	private int payeeDetailsId;

	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="t_sale_id")
	private Integer tSaleId;
	
	@Column(name="bill_amount")
	private Double billAmount;
	
	@Column(name="due_amount")
	private Double dueAmount;
	
	@Column(name = "disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name = "other_deduction")
	private Double otherDeduction;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;
	
	@Column(name="updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name = "cr_amount")
	private Double crAmount;
	
	@Column(name = "settlement_date")
	@Convert(converter=DateConverter.class)
	private Long settlementDate;
	
	@Column(name="associate_company_id")
	private Integer associateCompanyId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_sale_id", insertable = false, nullable = false, updatable = false)
	private Sale sale;

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public Double getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(Double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public int getPayeeDetailsId() {
		return payeeDetailsId;
	}

	public void setPayeeDetailsId(int payeeDetailsId) {
		this.payeeDetailsId = payeeDetailsId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer gettSaleId() {
		return tSaleId;
	}

	public void settSaleId(Integer tSaleId) {
		this.tSaleId = tSaleId;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
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

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Double getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(Double crAmount) {
		this.crAmount = crAmount;
	}

	public Long getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Long settlementDate) {
		this.settlementDate = settlementDate;
	}


	

}
