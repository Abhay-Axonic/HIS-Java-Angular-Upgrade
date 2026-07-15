package com.param.global.dto;

import javax.persistence.Column;

public class TPackageConsumptionServiceDetailDto {
	private Integer pacakgeConsumptionServiceDetailId;
	private Integer packageConsumptionMasterId;
	private Integer serviceId;
	private Integer packageQuantity;
	private Integer balancePackageQuantity;
	private Double apportionedPrice;
	private Integer isServiceItem;
	private Integer itemId;
	private String itemName;
	private Integer packageEoDetailId;
	private Integer organisationId;
	private Integer unitId;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private char status;
	private int isInclusiveExclusive;
	private String serviceCode;
	private String serviceName;
	private String specialityName;
	private Integer specialityId;
	private Integer subSpecialityId;
	private String subSpecialityName;
	private char isCapService;
	private char isPackageService;
	private Integer packageId;
	private Double balanceCapAmount;
	private Integer packageTypeId;
	private Integer isOrderedCancelled;
	private String empName;
	private String packageName;
	private Integer orderId;
	private String packageCode;
	private Double packagePrice;
	private int isCap;
	private Double serviceCap;
	private Double availableCap;
	private Integer isGroupwiseExlusion;
	
	// For inclusive Exclusive Group
	private Integer deptId;
	private Integer subDeptId;
	private Integer productCategoryId;
	
	private String orderNo;
	private String userName;
	
	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Double getBalanceCapAmount() {
		return balanceCapAmount;
	}

	public void setBalanceCapAmount(Double balanceCapAmount) {
		this.balanceCapAmount = balanceCapAmount;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public char getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(char isPackageService) {
		this.isPackageService = (isPackageService != '\u0000' ? isPackageService : 'N');
		;
	}

	public char getIsCapService() {
		return isCapService;
	}

	public void setIsCapService(char isCapService) {
		this.isCapService = (isCapService != '\u0000' ? isCapService : 'N');
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public Integer getPacakgeConsumptionServiceDetailId() {
		return pacakgeConsumptionServiceDetailId;
	}

	public void setPacakgeConsumptionServiceDetailId(Integer pacakgeConsumptionServiceDetailId) {
		this.pacakgeConsumptionServiceDetailId = pacakgeConsumptionServiceDetailId;
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

	public Integer getBalancePackageQuantity() {
		return balancePackageQuantity;
	}

	public void setBalancePackageQuantity(Integer balancePackageQuantity) {
		this.balancePackageQuantity = balancePackageQuantity;
	}

	public Double getApportionedPrice() {
		return apportionedPrice;
	}

	public void setApportionedPrice(Double apportionedPrice) {
		this.apportionedPrice = apportionedPrice;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getPackageEoDetailId() {
		return packageEoDetailId;
	}

	public void setPackageEoDetailId(Integer packageEoDetailId) {
		this.packageEoDetailId = packageEoDetailId;
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

	public int getIsInclusiveExclusive() {
		return isInclusiveExclusive;
	}

	public void setIsInclusiveExclusive(int isInclusiveExclusive) {
		this.isInclusiveExclusive = isInclusiveExclusive;
	}

	public Integer getIsOrderedCancelled() {
		return isOrderedCancelled;
	}

	public void setIsOrderedCancelled(Integer isOrderedCancelled) {
		this.isOrderedCancelled = isOrderedCancelled;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public Double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(Double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public int getIsCap() {
		return isCap;
	}

	public void setIsCap(int isCap) {
		this.isCap = isCap;
	}

	public Double getServiceCap() {
		return serviceCap;
	}

	public void setServiceCap(Double serviceCap) {
		this.serviceCap = serviceCap;
	}

	public Double getAvailableCap() {
		return availableCap;
	}

	public void setAvailableCap(Double availableCap) {
		this.availableCap = availableCap;
	}

	public Integer getIsGroupwiseExlusion() {
		return isGroupwiseExlusion;
	}

	public void setIsGroupwiseExlusion(Integer isGroupwiseExlusion) {
		this.isGroupwiseExlusion = isGroupwiseExlusion;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
