package com.param.pharmacy.master.unit.dto.storewiseGenericMapper;

import com.param.service.global.model.CookieObjectDto;

public abstract class StorewiseGenericMapperResponse extends CookieObjectDto{

	protected Integer id;

	protected Integer storeId;

	protected String storeName;

	protected Integer genericId;

	protected String genericCode;

	protected String genericName;

	protected Integer productCategoryId;

	protected String productCategory;

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer vedId;
	
	protected String vedName;
	
	protected Boolean status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getGenericCode() {
		return genericCode;
	}

	public void setGenericCode(String genericCode) {
		this.genericCode = genericCode;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
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

	public Integer getVedId() {
		return vedId;
	}

	public void setVedId(Integer vedId) {
		this.vedId = vedId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getVedName() {
		return vedName;
	}

	public void setVedName(String vedName) {
		this.vedName = vedName;
	}

}

/*
 * "id": 1, "storeId": "storeType":"", "genericId": "genericCode": ,
 * "genericName": "productCategoryId": "productCategory": "assetCategoryId":
 * "assetCategory": "status":
 */