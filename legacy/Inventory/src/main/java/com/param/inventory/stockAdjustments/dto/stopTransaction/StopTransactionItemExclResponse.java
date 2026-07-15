package com.param.inventory.stockAdjustments.dto.stopTransaction;

public class StopTransactionItemExclResponse {
	
	protected Integer id;

	protected String assetCategory;

	protected String productCategory;

	protected String itemCode;

	protected String itemName;
	
	protected Integer itemId;

	public StopTransactionItemExclResponse() {
		super();
	}

	public StopTransactionItemExclResponse(Integer id, String productCategory, String assetCategory, String code, String name,Integer itemId) { 
		this.id = id;
		this.productCategory = productCategory;
		this.assetCategory = assetCategory;
		this.itemCode = code;
		this.itemName = name;
		this.itemId = itemId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

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
	
	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
}
