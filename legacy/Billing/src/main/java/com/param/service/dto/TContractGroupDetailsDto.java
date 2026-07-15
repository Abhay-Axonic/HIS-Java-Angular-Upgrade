package com.param.service.dto;

import java.math.BigDecimal;

public class TContractGroupDetailsDto {

	private Integer contractGroupDetailId;
	
	private Integer contractId;
	
	private Integer departmentId;
	
	private Integer subDepartmentId;
	
	private Integer varianceUpDown;
	
	private BigDecimal variancePercentage;
	
	private BigDecimal coShare;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String groupDesc;
	
	private String subGroupDesc;
	
	private Character isPackage;
	
	
	private String contractName ;
	
	private Integer taxId;
	
	private Integer serviceId;
	
	private BigDecimal apportionedPercent;
	
	private Double rate;
	
	private Double taxPercentage;
	
	private Double finalRate;
	
	private Double selfPayable;
	
	private Double creditPayable;
	
	private Double apportionedPrice;
	
	
	
	
	
	public Double getApportionedPrice() {
		return apportionedPrice;
	}

	public void setApportionedPrice(Double apportionedPrice) {
		this.apportionedPrice = apportionedPrice;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getFinalRate() {
		return finalRate;
	}

	public void setFinalRate(Double finalRate) {
		this.finalRate = finalRate;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Character getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(Character isPackage) {
		this.isPackage = isPackage;
	}

	public BigDecimal getApportionedPercent() {
		return apportionedPercent;
	}

	public void setApportionedPercent(BigDecimal apportionedPercent) {
		this.apportionedPercent = apportionedPercent;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getSubGroupDesc() {
		return subGroupDesc;
	}

	public void setSubGroupDesc(String subGroupDesc) {
		this.subGroupDesc = subGroupDesc;
	}

	public Integer getContractGroupDetailId() {
		return contractGroupDetailId;
	}

	public void setContractGroupDetailId(Integer contractGroupDetailId) {
		this.contractGroupDetailId = contractGroupDetailId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	public Integer getVarianceUpDown() {
		return varianceUpDown;
	}

	public void setVarianceUpDown(Integer varianceUpDown) {
		this.varianceUpDown = varianceUpDown;
	}

	public BigDecimal getVariancePercentage() {
		return variancePercentage;
	}

	public void setVariancePercentage(BigDecimal variancePercentage) {
		this.variancePercentage = variancePercentage;
	}


	public BigDecimal getCoShare() {
		return coShare;
	}

	public void setCoShare(BigDecimal coShare) {
		this.coShare = coShare;
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

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	
	
}
