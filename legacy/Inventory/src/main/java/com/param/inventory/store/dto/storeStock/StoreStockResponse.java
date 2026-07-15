package com.param.inventory.store.dto.storeStock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
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
	protected String genericName;
	protected String menufactureName;
	protected Integer genericId;
	protected boolean isNarcotic;

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getMenufactureName() {
		return menufactureName;
	}

	public void setMenufactureName(String menufactureName) {
		this.menufactureName = menufactureName;
	}

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

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public boolean isNarcotic() {
		return isNarcotic;
	}

	public void setNarcotic(boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}
	
	
}