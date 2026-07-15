package com.param.assetManagement.indent.assetIssueAcceptance.dto;

public class AccessoriesRequest {

	protected Integer id;

	protected Integer accessoryId;

	protected Integer acceptedQuantity;

	protected Integer rejectQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Integer statusId;

	protected Integer issueAccessoriesId;

	protected Integer returnAccessoriesId;

	public AccessoriesRequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccessoryId() {
		return accessoryId;
	}

	public void setAccessoryId(Integer accessoryId) {
		this.accessoryId = accessoryId;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getIssueAccessoriesId() {
		return issueAccessoriesId;
	}

	public void setIssueAccessoriesId(Integer issueAccessoriesId) {
		this.issueAccessoriesId = issueAccessoriesId;
	}

	public Integer getReturnAccessoriesId() {
		return returnAccessoriesId;
	}

	public void setReturnAccessoriesId(Integer returnAccessoriesId) {
		this.returnAccessoriesId = returnAccessoriesId;
	}

}
