package com.param.global.dto;

public class PeriodicityServiceResDto {
	private String serviceName;
	private String	serviceCode;
	private Integer serviceId;
	private String department;
	private String subDepartment;
	private String serviceStandardCode;
	private Double basePrice;
	private Integer productCategoryId;
	private Integer assetCategoryId;
	private Integer periodicityServiceConfigId;
	private Integer equipmentId;
	private String equipmentName;
	private String displayName;
	private Integer visitTypeId;
	private Integer unitIdId;
	
	public Integer getUnitIdId() {
		return unitIdId;
	}
	public void setUnitIdId(Integer unitIdId) {
		this.unitIdId = unitIdId;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Integer getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public Integer getPeriodicityServiceConfigId() {
		return periodicityServiceConfigId;
	}
	public void setPeriodicityServiceConfigId(Integer periodicityServiceConfigId) {
		this.periodicityServiceConfigId = periodicityServiceConfigId;
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
	public Double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSubDepartment() {
		return subDepartment;
	}
	public void setSubDepartment(String subDepartment) {
		this.subDepartment = subDepartment;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceStandardCode() {
		return serviceStandardCode;
	}
	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}
}
