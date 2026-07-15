package com.param.inventory.store.dto.storeIndent;

public class ItemRequest {

	protected Integer id;

	protected Integer itemId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer quantity;

	protected Integer statusId;

	/* Use for material Return */
	protected Long batchId;

	protected Integer returnQuantity;

	protected Integer returnReasonId;

	protected String returnReason; /* For reject material */

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Integer rejectedQty;

	protected Integer acceptedQuantity;
	
	protected Integer issueQty;
	protected Integer comsumedQty;
	protected Integer issueDetailId;
	public Integer getIssueDetailId() {
		return issueDetailId;
	}

	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}

	public Integer getIssueQty() {
		return issueQty;
	}

	public void setIssueQty(Integer issueQty) {
		this.issueQty = issueQty;
	}

	public Integer getComsumedQty() {
		return comsumedQty;
	}

	public void setComsumedQty(Integer comsumedQty) {
		this.comsumedQty = comsumedQty;
	}

	public ItemRequest() {
		super();
	}

	public ItemRequest(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer quantity,
			Integer statusId) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.quantity = quantity;
		this.statusId = statusId;
	}

	public ItemRequest(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
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

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
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

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getReturnReasonId() {
		return returnReasonId;
	}

	public void setReturnReasonId(Integer returnReasonId) {
		this.returnReasonId = returnReasonId;
	}

	public Integer getRejectedQty() {
		return rejectedQty;
	}

	public void setRejectedQty(Integer rejectedQty) {
		this.rejectedQty = rejectedQty;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
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

}