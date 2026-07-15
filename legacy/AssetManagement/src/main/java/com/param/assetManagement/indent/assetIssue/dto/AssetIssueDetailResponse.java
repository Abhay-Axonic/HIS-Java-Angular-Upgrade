package com.param.assetManagement.indent.assetIssue.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;

@JsonInclude(Include.NON_NULL)
public class AssetIssueDetailResponse {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected String genericName;

	protected Integer indentQuantity;

	protected Integer pendingQuantity;

	protected Long issueQuantity;

	protected Long stockAvailable;

	protected Long batchId;

	protected String batchNo;

	protected Date expiry;

	protected List<BatchResponse> batch;
	protected List<BatchResponse> batches;
	public List<BatchResponse> getBatches() {
		return batches;
	}

	public void setBatches(List<BatchResponse> batches) {
		this.batches = batches;
	}

	protected List<IssueBatch> issueBatch;

	protected List<AsseccoriesResponse> asseccories;

	public AssetIssueDetailResponse() {
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public Long getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Long issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
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

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public List<IssueBatch> getIssueBatch() {
		return issueBatch;
	}

	public void setIssueBatch(List<IssueBatch> issueBatch) {
		this.issueBatch = issueBatch;
	}

	public void addBatches(BatchResponse batch) {
		if (this.batch == null)
			this.batch = new ArrayList<BatchResponse>();

		this.batch.add(batch);
	}

	public void addBatch(IssueBatch batch) {
		if (this.issueBatch == null)
			this.issueBatch = new ArrayList<IssueBatch>();

		this.issueBatch.add(batch);
	}

}
