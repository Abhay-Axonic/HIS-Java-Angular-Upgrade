package com.param.global.dto.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AsseccoriesResponse {

	protected Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	protected Long batchId;

	protected Integer issueQuantity;

	protected String batchNo;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Integer availableQty;
	protected Integer quantity;
	protected String specification;
	protected Integer approvedQty;
	
	

	public String getSpecification() {
		return specification;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getApprovedQty() {
		return approvedQty;
	}

	public void setApprovedQty(Integer approvedQty) {
		this.approvedQty = approvedQty;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	

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
	
	public AsseccoriesResponse( Integer id,Integer quantity, String specification, Integer approvedQty) {
		super();
		this.id=id;
		this.quantity = quantity;
		this.specification = specification;
		this.approvedQty = approvedQty;
		
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
