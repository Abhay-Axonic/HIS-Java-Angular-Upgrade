package com.param.global.dto.global;

public class GRNAgainstApprovedPOQuantity {
	
	Long quantity;
	
	Integer intQuantity;
	
	Integer storeId;
	
	public Integer getIntQuantity() {
		return intQuantity;
	}

	public void setIntQuantity(Integer intQuantity) {
		this.intQuantity = intQuantity;
	}

	Integer itemId;

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public GRNAgainstApprovedPOQuantity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
