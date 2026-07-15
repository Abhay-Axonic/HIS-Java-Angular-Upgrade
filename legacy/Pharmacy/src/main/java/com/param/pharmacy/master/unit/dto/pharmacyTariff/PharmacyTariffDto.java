package com.param.pharmacy.master.unit.dto.pharmacyTariff;

import java.util.List;

public class PharmacyTariffDto {

	private Integer id;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer assetCategoryId;
	
	private Integer productCategoryId;
	
	private Integer markUpDown;
	
	private Double markUpDownPer;
	
	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;
	
	private Integer paymentEntitlementId;
	
	private String assetCategoryName;
	
	private String productCategoryName;
	
	private List<PharmacyTariffDto> pharmacyTariffDtoList;
	
	public String getAssetCategoryName() {
		return assetCategoryName;
	}

	public void setAssetCategoryName(String assetCategoryName) {
		this.assetCategoryName = assetCategoryName;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public List<PharmacyTariffDto> getPharmacyTariffDtoList() {
		return pharmacyTariffDtoList;
	}

	public void setPharmacyTariffDtoList(List<PharmacyTariffDto> pharmacyTariffDtoList) {
		this.pharmacyTariffDtoList = pharmacyTariffDtoList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

}
