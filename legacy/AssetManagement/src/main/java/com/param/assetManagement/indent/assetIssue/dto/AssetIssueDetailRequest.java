package com.param.assetManagement.indent.assetIssue.dto;

import java.util.List;

import com.param.global.dto.items.BatchResponse;

public class AssetIssueDetailRequest {

	protected Integer id;

	protected Integer itemId;

	protected Integer statusId;

	protected Integer issueQuantity;

	protected List<BatchResponse> batches;

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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public List<BatchResponse> getBatches() {
		return batches;
	}

	public void setBatches(List<BatchResponse> batches) {
		this.batches = batches;
	}

}
