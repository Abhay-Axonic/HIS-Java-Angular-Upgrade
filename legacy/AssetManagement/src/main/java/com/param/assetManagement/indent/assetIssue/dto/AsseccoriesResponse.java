package com.param.assetManagement.indent.assetIssue.dto;

public class AsseccoriesResponse {

	protected String batchNo;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Integer availableQty;

	public AsseccoriesResponse() {
		super();
	}

	public AsseccoriesResponse(String batchNo, Integer itemId, String itemCode, String itemName, Integer availableQty) {
		super();
		this.batchNo = batchNo;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.availableQty = availableQty;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public Integer getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}

}
