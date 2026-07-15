package com.param.global.dto;

import java.math.BigDecimal;

public class PackageConsumptionEODetailDto {
	
	private Integer packageConsumptionEODetailId;
	private Integer packageConsumptionMasterId;
	private Integer productCatId;
	private Integer isServiceItemBed;
	private Integer packageEOGroupId;
	private Integer bedCatId;
	private Integer totalQty;
	private Integer balanceQty;
	private BigDecimal groupPrice;
	public Integer getPackageConsumptionEODetailId() {
		return packageConsumptionEODetailId;
	}
	public void setPackageConsumptionEODetailId(Integer packageConsumptionEODetailId) {
		this.packageConsumptionEODetailId = packageConsumptionEODetailId;
	}
	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}
	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}
	public Integer getProductCatId() {
		return productCatId;
	}
	public void setProductCatId(Integer productCatId) {
		this.productCatId = productCatId;
	}
	public Integer getIsServiceItemBed() {
		return isServiceItemBed;
	}
	public void setIsServiceItemBed(Integer isServiceItemBed) {
		this.isServiceItemBed = isServiceItemBed;
	}
	public Integer getPackageEOGroupId() {
		return packageEOGroupId;
	}
	public void setPackageEOGroupId(Integer packageEOGroupId) {
		this.packageEOGroupId = packageEOGroupId;
	}
	public Integer getBedCatId() {
		return bedCatId;
	}
	public void setBedCatId(Integer bedCatId) {
		this.bedCatId = bedCatId;
	}
	public Integer getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
	public Integer getBalanceQty() {
		return balanceQty;
	}
	public void setBalanceQty(Integer balanceQty) {
		this.balanceQty = balanceQty;
	}
	public BigDecimal getGroupPrice() {
		return groupPrice;
	}
	public void setGroupPrice(BigDecimal groupPrice) {
		this.groupPrice = groupPrice ;
	}

	
}
