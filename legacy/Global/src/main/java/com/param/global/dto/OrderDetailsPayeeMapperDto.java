package com.param.global.dto;

import java.math.BigDecimal;

public class OrderDetailsPayeeMapperDto {

	private Integer id;

	private Integer orderDetailsId;

	private Integer serviceId;

	private Integer contractId;

	private Integer payeeId;
	
	private Integer associateCompanyId;
	
	private String associateCompanyDesc;

	private String payeeDesc;

	private BigDecimal payeeAmount;
	
	private BigDecimal creditPayable;
	
	private BigDecimal selfPayable;
	
	private BigDecimal spiltBillAmt;

	private Integer unitId;

	private Integer organizationId;

	private Character status;
	
	private Integer tSaleDetailsId;
	
	private Integer itemId;
	
	private Character isRateEditable;
	
	private Character isTariffRateApplicable;
	
	private Double coShare;
	
	private Character isInvoiceGenerated;
	
	private Double quantity;
	
	private Integer priority;
	
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Character getIsInvoiceGenerated() {
		return isInvoiceGenerated;
	}

	public void setIsInvoiceGenerated(Character isInvoiceGenerated) {
		this.isInvoiceGenerated = isInvoiceGenerated;
	}

	public String getAssociateCompanyDesc() {
		return associateCompanyDesc;
	}

	public void setAssociateCompanyDesc(String associateCompanyDesc) {
		this.associateCompanyDesc = associateCompanyDesc;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public BigDecimal getSpiltBillAmt() {
		return spiltBillAmt;
	}

	public void setSpiltBillAmt(BigDecimal spiltBillAmt) {
		this.spiltBillAmt = spiltBillAmt;
	}

	public Character getIsTariffRateApplicable() {
		return isTariffRateApplicable;
	}
	
	public void setIsTariffRateApplicable(Character isTariffRateApplicable) {
		this.isTariffRateApplicable = isTariffRateApplicable;
	}

	public Double getCoShare() {
		return coShare;
	}

	public void setCoShare(Double coShare) {
		this.coShare = coShare;
	}

	public Character getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(Character isRateEditable) {
		this.isRateEditable = isRateEditable;
	}

	public String getPayeeDesc() {
		return payeeDesc;
	}

	public void setPayeeDesc(String payeeDesc) {
		this.payeeDesc = payeeDesc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public BigDecimal getPayeeAmount() {
		return payeeAmount;
	}

	public void setPayeeAmount(BigDecimal payeeAmount) {
		this.payeeAmount = payeeAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public BigDecimal getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(BigDecimal creditPayable) {
		this.creditPayable = creditPayable;
	}

	public BigDecimal getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(BigDecimal selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Integer gettSaleDetailsId() {
		return tSaleDetailsId;
	}

	public void settSaleDetailsId(Integer tSaleDetailsId) {
		this.tSaleDetailsId = tSaleDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

}
