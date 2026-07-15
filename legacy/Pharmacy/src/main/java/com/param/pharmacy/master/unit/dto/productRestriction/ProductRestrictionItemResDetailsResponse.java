package com.param.pharmacy.master.unit.dto.productRestriction;

import java.math.BigInteger;

public class ProductRestrictionItemResDetailsResponse extends ProductRestrictionResponse {

	protected String itemCode;

	protected String itemName;

	
	/*
	public ProductRestrictionItemResDetailsResponse(BigInteger id, Integer storeId, String storeName,
			Integer assetCategoryId, String assetCategory, Integer productCategoryId, String productcategory,
			Boolean status) {
		super(id, storeId, storeName, assetCategoryId, assetCategory, productCategoryId, productcategory, status);
		// TODO Auto-generated constructor stub
	}

	public ProductRestrictionItemResDetailsResponse(BigInteger id, Integer storeId, String storeName,
			Integer assetCategoryId, String assetCategory, Integer productCategoryId, String productcategory,
			Boolean status, String itemCode, String itemName) {
		super(id, storeId, storeName, assetCategoryId, assetCategory, productCategoryId, productcategory, status);
		this.itemCode = itemCode;
		this.itemName = itemName;
	}*/

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

}
