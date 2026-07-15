package com.param.pharmacy.master.unit.dto.productRestriction;

import java.math.BigInteger;

public class ProductRestrictionDetailsResponse extends ProductRestrictionResponse {

	protected Integer assetTypeId;

	protected String assetType;

	
	/*
	public ProductRestrictionDetailsResponse(BigInteger id, Integer storeId, String storeName, Integer assetCategoryId,
			String assetCategory, Integer productCategoryId, String productcategory, Boolean status) {
		super(id, storeId, storeName, assetCategoryId, assetCategory, productCategoryId, productcategory, status);
		// TODO Auto-generated constructor stub
	}

	public ProductRestrictionDetailsResponse(BigInteger id, Integer storeId, String storeName, Integer assetCategoryId,
			String assetCategory, Integer productCategoryId, String productcategory, Boolean status,
			Integer assetTypeId, String assetType) {
		super(id, storeId, storeName, assetCategoryId, assetCategory, productCategoryId, productcategory, status);
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
	}*/

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

}
