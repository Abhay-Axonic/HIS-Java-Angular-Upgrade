package com.param.pharmacy.master.unit.dto.productRestriction;

import java.math.BigInteger;

import com.param.service.global.model.CookieObjectDto;

public abstract class ProductRestrictionResponse  extends CookieObjectDto{

	protected BigInteger id;

	protected Integer storeId;

	protected String storeName;

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer productCategoryId;

	protected String productCategory;

	protected Boolean status;
	
	public ProductRestrictionResponse() {
		super();
	}
	
	public ProductRestrictionResponse(BigInteger id, Integer storeId, String storeName, Integer assetCategoryId,
			String assetCategory, Integer productCategoryId, String productCategory, Boolean status) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.storeName = storeName;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.status = status;
	}
	
	public ProductRestrictionResponse(Integer storeId, String storeName, Integer assetCategoryId,
			String assetCategory, Integer productCategoryId, String productCategory, Boolean status) {
		super();
		this.storeId = storeId;
		this.storeName = storeName;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.status = status;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	/*public ProductRestrictionResponse(BigInteger id, Integer storeId, String storeName, Integer assetCategoryId,
			String assetCategory, Integer productCategoryId, String productcategory, Boolean status) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.storeName = storeName;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productcategory = productcategory;
		this.status = status;
	}*/

}

/*
 * "id": 1, "storeId" : 1, "storeName": "store1", "assetTypeId": "assetType":
 * "assetCategoryId": "assetCategory": "productCategoryId": "productcategory":
 * "status": true
 */