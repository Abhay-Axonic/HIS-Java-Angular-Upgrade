package com.param.global.dto;

import com.param.global.common.GlobalCommonDateUtils;

public class PackageConsumptionServiceDetailDto {

	private Integer packageConsumptionServiceDetailId;
	private Integer packageConsumptionMasterId;
	private Integer serviceId;
	private String serviceCode;
	private String serviceStandardName;
	private Integer packageQuantity;
	private Integer itemId;
	private String itemName;
	private String itemCode;
	private Integer balanceQuantity;
	private double apportinedPrice;
	private Integer isServiceItem;
	private Integer packageEODetailId;
	private Integer organisationId;
	private Integer unitId;
	private Integer specialityId;
	private String specialityName;
	private Integer subSpecialityId;
	private String subSpecialityName;
	private int isInclusiveExclusive;
	private Integer packageEitherOrGroupId;
	private Integer packageServiceDetailsId;
	private Character isOrdCanceled;
	private Double serviceCap;
	private Double availableCap;
	private Integer serviceBedCategoryId;// for room rent
	private Integer serviceProductCategoryId;// form pharamcy service
	private Integer productCategoryId;// for consumable
	private Integer consultationSpecialityId;
	
	public Integer getConsultationSpecialityId() {
		return consultationSpecialityId;
	}

	public void setConsultationSpecialityId(Integer consultationSpecialityId) {
		this.consultationSpecialityId = consultationSpecialityId;
	}

	public Double getServiceCap() {
		return serviceCap;
	}

	public void setServiceCap(Double serviceCap) {
		this.serviceCap = serviceCap;
	}

	public Integer getPackageEitherOrGroupId() {
		return packageEitherOrGroupId;
	}

	public void setPackageEitherOrGroupId(Integer packageEitherOrGroupId) {
		this.packageEitherOrGroupId = packageEitherOrGroupId;
	}

	public int getIsInclusiveExclusive() {
		return isInclusiveExclusive;
	}

	public void setIsInclusiveExclusive(int isInclusiveExclusive) {
		this.isInclusiveExclusive = isInclusiveExclusive;
	}

	public Integer getPackageConsumptionServiceDetailId() {
		return packageConsumptionServiceDetailId;
	}

	public void setPackageConsumptionServiceDetailId(Integer packageConsumptionServiceDetailId) {
		this.packageConsumptionServiceDetailId = packageConsumptionServiceDetailId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPackageQuantity() {
		return packageQuantity;
	}

	public void setPackageQuantity(Integer packageQuantity) {
		this.packageQuantity = packageQuantity;
	}

	public Integer getBalanceQuantity() {
		return balanceQuantity;
	}

	public void setBalanceQuantity(Integer balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
	}

	public double getApportinedPrice() {
		return apportinedPrice;
	}

	public void setApportinedPrice(double apportinedPrice) {
		this.apportinedPrice = apportinedPrice;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
	}

	public Integer getPackageEODetailId() {
		return packageEODetailId;
	}

	public void setPackageEODetailId(Integer packageEODetailId) {
		this.packageEODetailId = packageEODetailId;
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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getPackageServiceDetailsId() {
		return packageServiceDetailsId;
	}

	public void setPackageServiceDetailsId(Integer packageServiceDetailsId) {
		this.packageServiceDetailsId = packageServiceDetailsId;
	}

	public Character getIsOrdCanceled() {
		return isOrdCanceled;
	}

	public void setIsOrdCanceled(Character isOrdCanceled) {
		this.isOrdCanceled = (isOrdCanceled == null) ? 'N' : isOrdCanceled;
	}

	public Double getAvailableCap() {
		return  availableCap==null?0:GlobalCommonDateUtils.getAmount(availableCap);
	}

	public void setAvailableCap(Double availableCap) {
		this.availableCap = availableCap==null?0:GlobalCommonDateUtils.getAmount(availableCap);
	}

	public Integer getServiceBedCategoryId() {
		return serviceBedCategoryId;
	}

	public void setServiceBedCategoryId(Integer serviceBedCategoryId) {
		this.serviceBedCategoryId = serviceBedCategoryId;
	}

	public Integer getServiceProductCategoryId() {
		return serviceProductCategoryId;
	}

	public void setServiceProductCategoryId(Integer serviceProductCategoryId) {
		this.serviceProductCategoryId = serviceProductCategoryId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

}
