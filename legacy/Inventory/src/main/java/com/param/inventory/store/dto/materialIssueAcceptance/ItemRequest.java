package com.param.inventory.store.dto.materialIssueAcceptance;

public class ItemRequest {

	protected Integer id;

	protected Integer acceptedQuantity;

	protected Integer rejectQuantity;
	
	protected Integer rejectedRejectQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Integer statusId;

	protected Long batchId;

	protected Integer fromstoreId;

	protected Integer toStoreId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer batchMapperId;
	protected Boolean isConsignment;

	public Boolean isConsignment() {
		return isConsignment;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public void setConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	protected Integer returnDetailId;

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

	public ItemRequest() {
		super();
	}

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

	public Integer getRejectedRejectQuantity() {
		return rejectedRejectQuantity;
	}

	public void setRejectedRejectQuantity(Integer rejectedRejectQuantity) {
		this.rejectedRejectQuantity = rejectedRejectQuantity;
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

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getReturnDetailId() {
		return returnDetailId;
	}

	public void setReturnDetailId(Integer returnDetailId) {
		this.returnDetailId = returnDetailId;
	}

}
