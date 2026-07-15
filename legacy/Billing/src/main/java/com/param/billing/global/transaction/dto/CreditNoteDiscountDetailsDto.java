package com.param.billing.global.transaction.dto;

public class CreditNoteDiscountDetailsDto {
	
    private Integer creditNoteDiscountDtlId;
	
	private Integer creditNoteDetailId;
	
	private Integer billingDetailsId;
	
	private Integer discountCategoryId;
	
	private String discountCategory;
	
	private double discountPercentage;
	
	private double discountAmount;	
	
	private Integer discountGivenBy;
	
	private Integer unitId;

	private Integer orgnisationId;
	
	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	public Integer getCreditNoteDiscountDtlId() {
		return creditNoteDiscountDtlId;
	}

	public void setCreditNoteDiscountDtlId(Integer creditNoteDiscountDtlId) {
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

	public String getDiscountCategory() {
		return discountCategory;
	}

	public void setDiscountCategory(String discountCategory) {
		this.discountCategory = discountCategory;
	}

	public Integer getBillingDetailsId() {
		return billingDetailsId;
	}

	public void setBillingDetailsId(Integer billingDetailsId) {
		this.billingDetailsId = billingDetailsId;
	}

}
