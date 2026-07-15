package com.param.global.dto;

import java.math.BigDecimal;

public class OrderDetailsPackageMapperDto {

	private Integer id;

	private Integer orderDetailsId;

	private Integer billDetailsId;

	private Integer serviceId;

	private Integer packageId;

	private Integer payeeId;

	private BigDecimal serviceAmount;

	private BigDecimal packageAmount;

	private Character isPackageService;

	private Character isCapService;

	private Integer saleDetailsId;

	private Integer itemId;

	private Character status;

	private Integer unitId;

	private Integer organizationId;

	private String packageName;

	private String packageCode;

	private String serviceStandardName;

	private String serviceStandardCode;

	private String itemCode;

	private String itemName;

	private Integer packageConsumptionCapDetailsId;

	private Integer packageConsumptionMasterId;

	private Integer pacakgeConsumptionServiceDetailId;

	private Integer packageConsumptionEODetailId;

	private Integer packageQty;

	private Integer tSaleDetailsId;
	
	private Integer packageTypeId;
	
	private Integer forceAddRemoved;
	
	private Character checked;


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

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount!=null?serviceAmount:BigDecimal.ZERO;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount!=null?serviceAmount:BigDecimal.ZERO;
	}

	public BigDecimal getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(BigDecimal packageAmount) {
		this.packageAmount = packageAmount;
	}

	public Integer getSaleDetailsId() {
		return saleDetailsId;
	}

	public void setSaleDetailsId(Integer saleDetailsId) {
		this.saleDetailsId = saleDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Character getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(Character isPackageService) {
		this.isPackageService = (isPackageService == null) ? 'N' : isPackageService;
	}

	public Character getIsCapService() {
		return isCapService;
	}

	public void setIsCapService(Character isCapService) {
		this.isCapService = (isCapService == null) ? 'N' : isCapService;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public Integer getBillDetailsId() {
		return billDetailsId;
	}

	public void setBillDetailsId(Integer billDetailsId) {
		this.billDetailsId = billDetailsId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public Integer getPackageConsumptionCapDetailsId() {
		return packageConsumptionCapDetailsId;
	}

	public void setPackageConsumptionCapDetailsId(Integer packageConsumptionCapDetailsId) {
		this.packageConsumptionCapDetailsId = packageConsumptionCapDetailsId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getPacakgeConsumptionServiceDetailId() {
		return pacakgeConsumptionServiceDetailId;
	}

	public void setPacakgeConsumptionServiceDetailId(Integer pacakgeConsumptionServiceDetailId) {
		this.pacakgeConsumptionServiceDetailId = pacakgeConsumptionServiceDetailId;
	}

	public Integer getPackageConsumptionEODetailId() {
		return packageConsumptionEODetailId;
	}

	public void setPackageConsumptionEODetailId(Integer packageConsumptionEODetailId) {
		this.packageConsumptionEODetailId = packageConsumptionEODetailId;
	}

	public Integer getPackageQty() {
		return packageQty;
	}

	public void setPackageQty(Integer packageQty) {
		this.packageQty = packageQty;
	}

	public Integer gettSaleDetailsId() {
		return tSaleDetailsId;
	}

	public void settSaleDetailsId(Integer tSaleDetailsId) {
		this.tSaleDetailsId = tSaleDetailsId;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Integer getForceAddRemoved() {
		return forceAddRemoved;
	}

	public void setForceAddRemoved(Integer forceAddRemoved) {
		this.forceAddRemoved = forceAddRemoved;
	}

	public Character getChecked() {
		return checked;
	}

	public void setChecked(Character checked) {
		this.checked = checked;
	}
	
}
