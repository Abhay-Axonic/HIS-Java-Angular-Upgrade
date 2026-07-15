package com.param.inventory.store.dto.shelfAllocation;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ShelfAllocationRequest extends CookieObjectDto {
	
	protected Integer id;
	
	protected Integer storeId;

	protected Integer assetCategoryId;

	protected Integer productCategoryId;
	
	protected Integer itemId;

	protected String itemCode;

	protected String itemName;
	
	protected Long batchId;
	
	protected String batchNo;
	
	protected Boolean allocated;
	
	protected Boolean status;
	
	protected List<RackShelfRequest> shelf;

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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Boolean getAllocated() {
		return allocated;
	}

	public void setAllocated(Boolean allocated) {
		this.allocated = allocated;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<RackShelfRequest> getShelf() {
		return shelf;
	}

	public void setShelf(List<RackShelfRequest> shelf) {
		this.shelf = shelf;
	}
}