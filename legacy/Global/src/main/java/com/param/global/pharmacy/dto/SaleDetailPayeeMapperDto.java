package com.param.global.pharmacy.dto;

import java.math.BigDecimal;

public class SaleDetailPayeeMapperDto {
	
	private Integer id;
	private Integer tSaleDetailsId;
	private String payeeDesc;
	private Integer itemId;
	private Integer contractId;
	private Integer payeeId;
	private Integer associateCompanyId;
	private BigDecimal payeeAmount;
	private BigDecimal spiltBillAmt;
	private Integer unitId;
	private Integer organizationId;
	private Character status;
	private Integer quantity;
	private int isTariffRateApplicable;
	private double selfCoPay;
	private BigDecimal unitPayeeRate;
	private Integer productCategoryId;
	
	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		this.payeeAmount = payeeAmount;
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

	public String getPayeeDesc() {
		return payeeDesc;
	}

	public void setPayeeDesc(String payeeDesc) {
		this.payeeDesc = payeeDesc;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSpiltBillAmt() {
		return spiltBillAmt;
	}

	public void setSpiltBillAmt(BigDecimal spiltBillAmt) {
		this.spiltBillAmt = spiltBillAmt;
	}

	public int getIsTariffRateApplicable() {
		return isTariffRateApplicable;
	}

	public void setIsTariffRateApplicable(int isTariffRateApplicable) {
		this.isTariffRateApplicable = isTariffRateApplicable;
	}

	public double getSelfCoPay() {
		return selfCoPay;
	}

	public void setSelfCoPay(double selfCoPay) {
		this.selfCoPay = selfCoPay;
	}

	public BigDecimal getUnitPayeeRate() {
		return unitPayeeRate==null?BigDecimal.ZERO : unitPayeeRate;
	}

	public void setUnitPayeeRate(BigDecimal unitPayeeRate) {
		this.unitPayeeRate = unitPayeeRate;
	}
	
	
}
