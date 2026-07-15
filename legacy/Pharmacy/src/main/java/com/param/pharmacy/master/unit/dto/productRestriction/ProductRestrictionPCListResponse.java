package com.param.pharmacy.master.unit.dto.productRestriction;

import java.math.BigInteger;

public class ProductRestrictionPCListResponse extends ProductRestrictionResponse {

	protected Integer assetTypeId;

	protected String assetType;

	
	public ProductRestrictionPCListResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductRestrictionPCListResponse(BigInteger id, Integer storeId, String storeName, Integer assetCategoryId,
			String assetCategory, Integer productCategoryId, String productcategory, Boolean status,
			Integer assetTypeId, String assetType) {
		super(id, storeId, storeName, assetCategoryId, assetCategory, productCategoryId, productcategory, status);
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
	}
	
	public ProductRestrictionPCListResponse(Integer storeId, String storeName, Integer assetCategoryId,
			String assetCategory, Integer productCategoryId, String productcategory, Boolean status,
			Integer assetTypeId, String assetType) {
		super(storeId, storeName, assetCategoryId, assetCategory, productCategoryId, productcategory, status);
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
	}

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
