package com.param.pharmacy.master.unit.dto;

public class StoreStockResponse {

	protected Integer id;

	protected String itemCode;

	protected String itemName;

	protected String assetCategory;

	protected String productCategory;

	protected Integer minimumStock;

	protected Long currentStock;

	protected Long stockInTransit;

	protected Long pendingIndentQuantity;

	public StoreStockResponse() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getMinimumStock() {
		return minimumStock;
	}

	public void setMinimumStock(Integer minimumStock) {
		this.minimumStock = minimumStock;
	}

	public Long getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Long currentStock) {
		this.currentStock = currentStock;
	}

	public Long getStockInTransit() {
		return stockInTransit;
	}

	public void setStockInTransit(Long stockInTransit) {
		this.stockInTransit = stockInTransit;
	}

	public Long getPendingIndentQuantity() {
		return pendingIndentQuantity;
	}

	public void setPendingIndentQuantity(Long pendingIndentQuantity) {
		this.pendingIndentQuantity = pendingIndentQuantity;
	}

}
