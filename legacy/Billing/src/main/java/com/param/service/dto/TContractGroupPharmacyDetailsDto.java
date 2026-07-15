package com.param.service.dto;

import java.util.Date;
import java.util.List;

public class TContractGroupPharmacyDetailsDto {

	private Integer contractGroupExclusionDetailId;
	
	private Integer contractId;
	
	private Integer departmentId;
	
	private Integer subDepartmentId;
	
	private Integer categoryId;
	
	private Integer serviceId;
	
	private Integer itemId;
	
	private Integer isServiceItem;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Date updatedDate;
	
	private Date createdDate;
	
	private Character isEquipment;
	
	private Double quantity;
	
	private Double qty;
	
	private Double amount;
	
	private String category;
	
	private String itemCode;
	
	private String itemName;
	
	private Integer productCategoryId;
	
	private Integer assetCategoryId;
	
	private List<Integer> productCategoryList;
	
	private List<Integer> assetCategoryList;
	
	private List<TContractGroupPharmacyDetailsDto> itemList;
	
	private List<TContractGroupPharmacyDetailsDto> equipmentList;
	
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

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Character getIsEquipment() {
		return isEquipment;
	}

	public void setIsEquipment(Character isEquipment) {
		this.isEquipment = isEquipment;
	}

	public List<TContractGroupPharmacyDetailsDto> getItemList() {
		return itemList;
	}

	public void setItemList(List<TContractGroupPharmacyDetailsDto> itemList) {
		this.itemList = itemList;
	}

	public List<TContractGroupPharmacyDetailsDto> getEquipmentList() {
		return equipmentList;
	}

	public void setEquipmentList(List<TContractGroupPharmacyDetailsDto> equipmentList) {
		this.equipmentList = equipmentList;
	}

	public Integer getContractGroupExclusionDetailId() {
		return contractGroupExclusionDetailId;
	}

	public void setContractGroupExclusionDetailId(Integer contractGroupExclusionDetailId) {
		this.contractGroupExclusionDetailId = contractGroupExclusionDetailId;
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

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<Integer> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<Integer> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}

	public List<Integer> getAssetCategoryList() {
		return assetCategoryList;
	}

	public void setAssetCategoryList(List<Integer> assetCategoryList) {
		this.assetCategoryList = assetCategoryList;
	}
	
}
