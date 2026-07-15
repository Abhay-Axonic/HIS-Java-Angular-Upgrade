package com.param.inventory.scrap.dto.requestAssestDisposalWorklist;

public class ItemRequest {

	protected Integer id;

	protected Integer scrapRequestDetailId;

	protected Integer assetDisposalWorklistRequestDetailId;

	protected Integer disposeQuantity;

	protected Long batchId;

	protected Integer statusId;

	protected Integer approvedDisposedQuantity;

	protected Integer rejectQuantity;

	protected String rejectNote;

	protected Integer rejectReasonId;

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

	public Integer getScrapRequestDetailId() {
		return scrapRequestDetailId;
	}

	public void setScrapRequestDetailId(Integer scrapRequestDetailId) {
		this.scrapRequestDetailId = scrapRequestDetailId;
	}

	public Integer getAssetDisposalWorklistRequestDetailId() {
		return assetDisposalWorklistRequestDetailId;
	}

	public void setAssetDisposalWorklistRequestDetailId(Integer assetDisposalWorklistRequestDetailId) {
		this.assetDisposalWorklistRequestDetailId = assetDisposalWorklistRequestDetailId;
	}

	public Integer getDisposeQuantity() {
		return disposeQuantity;
	}

	public void setDisposeQuantity(Integer disposeQuantity) {
		this.disposeQuantity = disposeQuantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovedDisposedQuantity() {
		return approvedDisposedQuantity;
	}

	public void setApprovedDisposedQuantity(Integer approvedDisposedQuantity) {
		this.approvedDisposedQuantity = approvedDisposedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

}
