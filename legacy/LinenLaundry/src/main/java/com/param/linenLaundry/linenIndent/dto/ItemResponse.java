package com.param.linenLaundry.linenIndent.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;

@JsonInclude(Include.NON_NULL)
public class ItemResponse {
	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Integer quantity;

	protected Integer pendingQuantity;
	
	protected Integer issueQuantity;
	
	protected Long stockAvailable;
	
	protected Long stockInTransit;
	
	protected Integer statusId;
	
	protected String status;
	
	protected Long batchId;

	protected String batchNo;
	
	protected Integer batchMapperId;
	
	protected List<BatchResponse> batch;
	
	protected Integer returnQuantity;
	
	protected Integer returnReasonId;
	
	protected String returnReason;
	
	public ItemResponse() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Long getStockInTransit() {
		return stockInTransit;
	}

	public void setStockInTransit(Long stockInTransit) {
		this.stockInTransit = stockInTransit;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
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

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getReturnReasonId() {
		return returnReasonId;
	}

	public void setReturnReasonId(Integer returnReasonId) {
		this.returnReasonId = returnReasonId;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public void addBatches(BatchResponse batch) {
		if (this.batch == null)
			this.batch = new ArrayList<BatchResponse>();

		this.batch.add(batch);
	}
	
	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Long stockAvailable,
			Integer statusId, String status, Long batchId, String batchNo, Integer returnQuantity, 
			Integer returnReasonId, String returnReason) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.stockAvailable = stockAvailable;
		this.statusId = statusId;
		this.status = status;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.returnQuantity = returnQuantity;
		this.returnReasonId = returnReasonId;
		this.returnReason = returnReason;
	}

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			Long stockAvailable, Long stockInTransit, Integer statusId, String status) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.stockAvailable = stockAvailable;
		this.stockInTransit = stockInTransit;
		this.statusId = statusId;
		this.status = status;
	}
	
	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			Long stockAvailable, Integer pendingQuantity, Integer issueQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.stockAvailable = stockAvailable;
		this.pendingQuantity = pendingQuantity;
		this.issueQuantity = issueQuantity;
	}

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			Integer pendingQuantity, Integer issueQuantity, Long batchId, String batchNo) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.pendingQuantity = pendingQuantity;
		this.issueQuantity = issueQuantity;
		this.batchId = batchId;
		this.batchNo = batchNo;
	} 

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			Integer pendingQuantity, Long batchId, String batchNo, Integer batchMapperId, Integer issueQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.pendingQuantity = pendingQuantity;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.batchMapperId = batchMapperId;
		this.issueQuantity = issueQuantity;
	}
	
}