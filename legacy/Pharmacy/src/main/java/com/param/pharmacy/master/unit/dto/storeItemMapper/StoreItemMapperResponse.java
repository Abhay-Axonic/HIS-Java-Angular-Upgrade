package com.param.pharmacy.master.unit.dto.storeItemMapper;

import java.math.BigInteger;

public abstract class StoreItemMapperResponse {

	protected BigInteger id;

	protected Integer storeId;

	protected String storeName;

	protected Integer itemId;

	protected String itemName;
	
	protected String itemCode;

	protected Integer productCategoryId;

	protected String productCategory;

	protected Integer assetCategoryId;

	protected String assetCategory;

	protected Integer minQty;

	protected Integer maxQty;

	protected Boolean status;
	
	protected Integer transactionCount;

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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(Integer transactionCount) {
		this.transactionCount = transactionCount;
	}

}

/*
 * "id": 1, "storeId" : 1, "storeName": "store1", "itemId": 1, "itemName":
 * "PRAXBIND SOL INJ 50MG/ML", "itemCode": "SANINJ2", "genericId": 1
 * "productCategoryId": 1 "productCategory": "ProdDescription1",
 * "assetCategoryId": 1 "assetCategory": "Implant", "minQty": 5 "maxQty": 10000
 */