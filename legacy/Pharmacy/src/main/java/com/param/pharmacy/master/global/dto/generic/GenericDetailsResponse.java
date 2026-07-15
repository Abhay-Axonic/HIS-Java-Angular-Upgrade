package com.param.pharmacy.master.global.dto.generic;

import java.util.List;

public class GenericDetailsResponse extends GenericResponse {

	protected Integer assetTypeId;

	protected Integer assetCategoryId;

	protected Integer productCategoryId;
	
	List<GenericRequestItem> items;

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

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public List<GenericRequestItem> getItems() {
		return items;
	}

	public void setItems(List<GenericRequestItem> items) {
		this.items = items;
	}
	
	

}
