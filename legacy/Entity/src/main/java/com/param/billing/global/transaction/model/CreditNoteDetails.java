package com.param.billing.global.transaction.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;

@Entity
@Table(name="t_credit_note_detail", schema="billing")
@SequenceGenerator(name = "seq_t_credit_note_detail", sequenceName = "billing.seq_t_credit_note_detail", allocationSize = 1)
public class CreditNoteDetails {

	@Id
	@Column(name = "credit_note_detail_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_credit_note_detail")
	private int creditNoteDetailId;
	
	@Column(name = "credit_note_id")
	private Integer creditNoteId;
	
	@Column(name = "cr_note_date")
	private Date crNoteDate;
	
	@Column(name = "order_dtl_id_old")
	private Integer orderDtlIdOld;
	
	@Column(name = "order_detail_id")
	private Integer orderDetailId;
	
	@Column(name = "order_detail_id_new")
	private Integer orderDetailIdNew;
	
	@Column(name = "cr_discount_amount")
	private double crDiscountAmount;
	
	@Column(name = "cr_service_rate_amount")
	private double crServiceRateAmount;
	
	@Column(name = "cr_additional_discount")
	private double crAdditionalDiscount;
	
	@Column(name = "cr_tax_amount")
	private double crTaxAmount;
	
	@Column(name = "cr_net_amount")
	private double crNetAmount;
	
	@Column(name = "billing_details_id")
	private Integer billingDetailsId;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "self_payable")
    private BigDecimal selfPayable;
	
	@Column(name = "credit_payable")
	private BigDecimal creditPayable;
	
	@Column(name = "package_consumption_amt")
	private BigDecimal packageConsumptionAmt;

	@Column(name = "cancel_or_discount")
	private Integer cancelOrDiscount;

	@Column(name = "status")
	private char status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "credit_note_id", insertable = false, updatable = false, nullable = false)
	private CreditNoteMaster creditNoteMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_details_id", insertable = false, nullable = false, updatable = false)
	private BillingDetails billingDetails;
	
	
	public BigDecimal getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(BigDecimal selfPayable) {
		this.selfPayable = selfPayable;
	}

	public BigDecimal getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(BigDecimal creditPayable) {
		this.creditPayable = creditPayable;
	}

	public BigDecimal getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(BigDecimal packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public int getCreditNoteDetailId() {
		return creditNoteDetailId;
	}

	public void setCreditNoteDetailId(int creditNoteDetailId) {
		this.creditNoteDetailId = creditNoteDetailId;
	}

	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public Date getCrNoteDate() {
		return crNoteDate;
	}

	public void setCrNoteDate(Date crNoteDate) {
		this.crNoteDate = crNoteDate;
	}

	public Integer getOrderDtlIdOld() {
		return orderDtlIdOld;
	}

	public void setOrderDtlIdOld(Integer orderDtlIdOld) {
		this.orderDtlIdOld = orderDtlIdOld;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public double getCrDiscountAmount() {
		return crDiscountAmount;
	}

	public void setCrDiscountAmount(double crDiscountAmount) {
		this.crDiscountAmount = crDiscountAmount;
	}

	public double getCrServiceRateAmount() {
		return crServiceRateAmount;
	}

	public void setCrServiceRateAmount(double crServiceRateAmount) {
		this.crServiceRateAmount = crServiceRateAmount;
	}

	public double getCrAdditionalDiscount() {
		return crAdditionalDiscount;
	}

	public void setCrAdditionalDiscount(double crAdditionalDiscount) {
		this.crAdditionalDiscount = crAdditionalDiscount;
	}

	public double getCrTaxAmount() {
		return crTaxAmount;
	}

	public void setCrTaxAmount(double crTaxAmount) {
		this.crTaxAmount = crTaxAmount;
	}

	public double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}


	public Integer getBillingDetailsId() {
		return billingDetailsId;
	}

	public void setBillingDetailsId(Integer billingDetailsId) {
		this.billingDetailsId = billingDetailsId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public CreditNoteMaster getCreditNoteMaster() {
		return creditNoteMaster;
	}

	public void setCreditNoteMaster(CreditNoteMaster creditNoteMaster) {
		this.creditNoteMaster = creditNoteMaster;
	}

	public Integer getOrderDetailIdNew() {
		return orderDetailIdNew;
	}

	public void setOrderDetailIdNew(Integer orderDetailIdNew) {
		this.orderDetailIdNew = orderDetailIdNew;
	}

	public Integer getCancelOrDiscount() {
		return cancelOrDiscount;
	}

	public void setCancelOrDiscount(Integer cancelOrDiscount) {
		this.cancelOrDiscount = cancelOrDiscount;
	}
	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}
}
