package com.param.assetManagement.indent.assetIssueAcceptance.dto;

import java.util.List;

public class ItemRequest {

	protected Integer id;

	protected Integer acceptedQuantity;

	protected Integer rejectQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Integer statusId;

	protected Long batchId;

	protected Integer fromstoreId;

	protected Integer toStoreId;

	protected Integer batchMapperId;

	protected Integer issueBatchMapperId;

	protected Integer issueReturnDetailId;

	protected List<AccessoriesRequest> accessories;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getFromstoreId() {
		return fromstoreId;
	}

	public void setFromstoreId(Integer fromstoreId) {
		this.fromstoreId = fromstoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public List<AccessoriesRequest> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<AccessoriesRequest> accessories) {
		this.accessories = accessories;
	}

	public Integer getIssueBatchMapperId() {
		return issueBatchMapperId;
	}

	public void setIssueBatchMapperId(Integer issueBatchMapperId) {
		this.issueBatchMapperId = issueBatchMapperId;
	}

	public Integer getIssueReturnDetailId() {
		return issueReturnDetailId;
	}

	public void setIssueReturnDetailId(Integer issueReturnDetailId) {
		this.issueReturnDetailId = issueReturnDetailId;
	}

}
