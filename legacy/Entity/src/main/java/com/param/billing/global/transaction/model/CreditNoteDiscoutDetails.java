package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name="GET_CREDIT_NOTE_DISOUNCT_DETAILS", 
			query=" SELECT crDiscountDtls.creditNoteDiscountDtlId AS creditNoteDiscountDtlId, "
					+ " crDiscountDtls.creditNoteDetailId AS creditNoteDetailId, "
					+ " crDiscountDtls.discountCategoryId AS discountCategoryId, "
					+ " crDiscountDtls.discountPercentage AS discountPercentage, "
					+ " crDiscountDtls.discountAmount AS discountAmount ,  "
					+ " discountGivenBy AS discountGivenBy "
					+ " FROM CreditNoteDiscoutDetails crDiscountDtls "
					+ " WHERE crDiscountDtls.billingDetailsId =:billingDetailsId ")
	})

@Entity
@Table(name="t_credit_note_discount_detail", schema="billing")
@SequenceGenerator(name = "seq_t_credit_note_discount_detail", sequenceName = "billing.seq_t_credit_note_discount_detail", allocationSize = 1)
public class CreditNoteDiscoutDetails {
	
	@Id
	@Column(name = "credit_note_discount_dtl_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_credit_note_discount_detail")
	private int creditNoteDiscountDtlId;
	
	@Column(name = "credit_note_detail_id")
	private Integer creditNoteDetailId;
	
	@Column(name = "discount_category_id")
	private Integer discountCategoryId;
	
	@Column(name = "discount_percentage")
	private double discountPercentage;
	
	@Column(name = "discount_amount")
	private double discountAmount;	
	
	@Column(name = "discount_given_by")
	private Integer discountGivenBy;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "billing_details_id")
	private Integer billingDetailsId;

	@Column(name = "organisation_id")
	private Integer orgnisationId;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "credit_note_detail_id", insertable = false, updatable = false, nullable = false)
	private CreditNoteDetails creditNoteDetails;
	

	public int getCreditNoteDiscountDtlId() {
		return creditNoteDiscountDtlId;
	}

	public void setCreditNoteDiscountDtlId(int creditNoteDiscountDtlId) {
		this.creditNoteDiscountDtlId = creditNoteDiscountDtlId;
	}

	public Integer getCreditNoteDetailId() {
		return creditNoteDetailId;
	}

	public void setCreditNoteDetailId(Integer creditNoteDetailId) {
		this.creditNoteDetailId = creditNoteDetailId;
	}

	public Integer getDiscountCategoryId() {
		return discountCategoryId;
	}

	public void setDiscountCategoryId(Integer discountCategoryId) {
		this.discountCategoryId = discountCategoryId;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Integer getDiscountGivenBy() {
		return discountGivenBy;
	}

	public void setDiscountGivenBy(Integer discountGivenBy) {
		this.discountGivenBy = discountGivenBy;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
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

	public CreditNoteDetails getCreditNoteDetails() {
		return creditNoteDetails;
	}

	public void setCreditNoteDetails(CreditNoteDetails creditNoteDetails) {
		this.creditNoteDetails = creditNoteDetails;
	}

	public Integer getBillingDetailsId() {
		return billingDetailsId;
	}

	public void setBillingDetailsId(Integer billingDetailsId) {
		this.billingDetailsId = billingDetailsId;
	}
	
	

}
