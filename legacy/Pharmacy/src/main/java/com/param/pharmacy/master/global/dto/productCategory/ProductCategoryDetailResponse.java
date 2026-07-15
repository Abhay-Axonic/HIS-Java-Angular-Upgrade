package com.param.pharmacy.master.global.dto.productCategory;

public class ProductCategoryDetailResponse extends ProductCategoryResponse {

	protected Integer assetTypeId;

	protected Integer assetCategoryId;

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}
}