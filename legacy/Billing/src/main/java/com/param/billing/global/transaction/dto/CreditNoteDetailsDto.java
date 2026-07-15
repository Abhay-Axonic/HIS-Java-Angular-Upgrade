package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreditNoteDetailsDto {

	private Integer creditNoteDetailId;

	private Integer creditNoteId;

	private String crNoteDate;

	private Integer orderDtlIdOld;

	private Integer orderDetailId;

	private Integer orderDetailIdNew;

	private double crDiscountAmount;

	private double crServiceRateAmount;

	private double crAdditionalDiscount;

	private double crTaxAmount;

	private double crNetAmount;

	private Integer organisationId;

	private Integer unitId;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;

	private Integer orderId;

	private Integer orderDetailsId;

	private Integer billingDetailsId;

	private Integer crReasonId;

	private Integer cancelOrDiscount;

	private Double serviceNewNetAmount;

	private BigDecimal selfPayable;

	private BigDecimal creditPayable;

	private BigDecimal packageConsumptionAmt;

	private Boolean isPackage;

	private Integer packageId;
	
	private Character status;
	
	private List<CreditNoteDiscountDetailsDto> listCreditNoteDiscountDetailsDto;
	
	private Integer packageTypeId;
	
	private Double hospitalShare;
	
	private Integer discountOwnership;
	
	private Double refDocSharePer;
	
	private Double docSharePer;
    
    public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

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

	public Double getServiceNewNetAmount() {
		return serviceNewNetAmount;
	}

	public void setServiceNewNetAmount(Double serviceNewNetAmount) {
		this.serviceNewNetAmount = serviceNewNetAmount;
	}

	public Integer getCancelOrDiscount() {
		return cancelOrDiscount;
	}

	public void setCancelOrDiscount(Integer cancelOrDiscount) {
		this.cancelOrDiscount = cancelOrDiscount;
	}

	public Integer getCreditNoteDetailId() {
		return creditNoteDetailId;
	}

	public void setCreditNoteDetailId(Integer creditNoteDetailId) {
		this.creditNoteDetailId = creditNoteDetailId;
	}

	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public String getCrNoteDate() {
		return crNoteDate;
	}

	public void setCrNoteDate(String crNoteDate) {
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrderDetailIdNew() {
		return orderDetailIdNew;
	}

	public void setOrderDetailIdNew(Integer orderDetailIdNew) {
		this.orderDetailIdNew = orderDetailIdNew;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getBillingDetailsId() {
		return billingDetailsId;
	}

	public void setBillingDetailsId(Integer billingDetailsId) {
		this.billingDetailsId = billingDetailsId;
	}

	public List<CreditNoteDiscountDetailsDto> getListCreditNoteDiscountDetailsDto() {
		return listCreditNoteDiscountDetailsDto;
	}

	public void setListCreditNoteDiscountDetailsDto(
			List<CreditNoteDiscountDetailsDto> listCreditNoteDiscountDetailsDto) {
		this.listCreditNoteDiscountDetailsDto = listCreditNoteDiscountDetailsDto;
	}

	public Integer getCrReasonId() {
		return crReasonId;
	}

	public void setCrReasonId(Integer crReasonId) {
		this.crReasonId = crReasonId;
	}

	public Boolean getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(Boolean isPackage) {
		this.isPackage = isPackage;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Double getHospitalShare() {
		return hospitalShare;
	}

	public void setHospitalShare(Double hospitalShare) {
		this.hospitalShare = hospitalShare;
	}

	public Integer getDiscountOwnership() {
		return discountOwnership;
	}

	public void setDiscountOwnership(Integer discountOwnership) {
		this.discountOwnership = discountOwnership;
	}

	public Double getRefDocSharePer() {
		return refDocSharePer;
	}

	public void setRefDocSharePer(Double refDocSharePer) {
		this.refDocSharePer = refDocSharePer;
	}

	public Double getDocSharePer() {
		return docSharePer;
	}

	public void setDocSharePer(Double docSharePer) {
		this.docSharePer = docSharePer;
	}

}
