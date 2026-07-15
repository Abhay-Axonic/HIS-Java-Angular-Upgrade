package com.param.procurement.consignment.dto.implantIndent;

public class ImplantIndentRequestItems {

	protected Integer id;

	protected Integer itemId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer indentQuantity;

	protected Integer statusId;
	

	protected Integer acceptedQuantity;

	protected Integer rejectQuantity;
	
	protected Integer rejectedRejectQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Long batchId;

	protected Integer batchMapperId;

	public ImplantIndentRequestItems() {
		super();
	}

	public ImplantIndentRequestItems(Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer indentQuantity,
			Integer statusId) {
		super();
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.indentQuantity = indentQuantity;
		this.statusId = statusId;
	}

	public ImplantIndentRequestItems(Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer statusId) {
		super();
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.statusId = statusId;
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

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

}
