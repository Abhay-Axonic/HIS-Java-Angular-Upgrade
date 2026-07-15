package com.param.pharmacy.master.unit.dto.productRestriction;

public class ProductRestrictionItemRestrictionRequest extends ProductRestrictionResponse {

	protected Integer itemId;
	
	protected String itemCode;

	protected String itemName;

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

}
