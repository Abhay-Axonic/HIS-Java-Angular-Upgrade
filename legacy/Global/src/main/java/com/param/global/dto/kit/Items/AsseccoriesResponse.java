package com.param.global.dto.kit.Items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AsseccoriesResponse {

	protected Long batchId;

	protected Integer issueQuantity;

	protected String batchNo;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Integer availableQty;

	public AsseccoriesResponse() {
		super();
	}

	public AsseccoriesResponse(Long batchId, Integer issueQuantity) {
		super();
		this.batchId = batchId;
		this.issueQuantity = issueQuantity;
	}
	public AsseccoriesResponse( Long batchId, String batchNo, Integer itemId, String itemCode, String itemName, Integer availableQty) {
			super();
			this.batchId = batchId;
			this.batchNo = batchNo;
			this.itemId = itemId;
			this.itemCode = itemCode;
			this.itemName = itemName;
			this.availableQty = availableQty;
		}
	
	public AsseccoriesResponse( Long batchId, String batchNo, Integer itemId, String itemCode, String itemName, Integer availableQty,
		Integer issueQuantity) {
		this(batchId, batchNo, itemId, itemCode, itemName, availableQty);
		this.issueQuantity = issueQuantity;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
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
