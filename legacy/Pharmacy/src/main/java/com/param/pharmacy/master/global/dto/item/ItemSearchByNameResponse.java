package com.param.pharmacy.master.global.dto.item;

public class ItemSearchByNameResponse {
	
	protected Integer itemId;

	protected String itemCode;
	
	protected String detail;

	protected String itemName;
	
	public ItemSearchByNameResponse() {
		super();	
	}
	
	public ItemSearchByNameResponse(Integer itemId, String detail, String itemCode, String itemName) 
	{
		super();
		this.itemId=itemId;
		this.detail=detail;
		this.itemCode=itemCode;
		this.itemName=itemName;
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
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
