package com.param.assetManagement.indent.assetIssue.dto;

public class IssueBatch {

	protected Integer id;

	protected Long batchId;

	protected Integer issueQuantity;

	public IssueBatch() {
		super();
	}

	public IssueBatch(Integer id, Long batchId, Integer issueQuantity) {
		super();
		this.id = id;
		this.batchId = batchId;
		this.issueQuantity = issueQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

}
