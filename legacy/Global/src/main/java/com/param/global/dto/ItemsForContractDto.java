package com.param.global.dto;

import java.util.List;

public class ItemsForContractDto {

	private Integer itemId;

	private Integer assetCategoryId;
	
	private String assetCategory;

	private Integer productCategoryId;
	
	private String productCategory;

	private String itemName;
	
	private String itemCode;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private List<Integer> productCategoryList;
	
	private List<Integer> assetCategoryList;
	
	private Integer visitTypeId;
	
	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	
}
