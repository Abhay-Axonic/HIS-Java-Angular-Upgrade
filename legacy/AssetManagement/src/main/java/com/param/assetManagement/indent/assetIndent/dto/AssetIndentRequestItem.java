package com.param.assetManagement.indent.assetIndent.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AssetIndentRequestItem {

	protected Integer id;

	protected Integer itemId;

	protected Integer quantity;

	protected Integer statusId;

	protected String specification;

	protected Integer approvedQuantity;

	protected Integer rejectQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Integer returnQuantity;

	protected Long batchId;

	protected Integer returnReasonId;

	protected Integer acceptedQuantity;
	protected Integer issueDetailId;
	
	protected Integer issueBatchMapperId;

	public Integer getIssueBatchMapperId() {
		return issueBatchMapperId;
	}

	public void setIssueBatchMapperId(Integer issueBatchMapperId) {
		this.issueBatchMapperId = issueBatchMapperId;
	}

	public Integer getIssueDetailId() {
		return issueDetailId;
	}

	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}

	protected List<AssetIndentRequestAccessories> accessories;
	
	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<AssetIndentRequestAccessories> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<AssetIndentRequestAccessories> accessories) {
		this.accessories = accessories;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
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

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getReturnReasonId() {
		return returnReasonId;
	}

	public void setReturnReasonId(Integer returnReasonId) {
		this.returnReasonId = returnReasonId;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

}