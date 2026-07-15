package com.param.inventory.stockAdjustments.dto.uploadStock;

import javax.persistence.Column;

public class UploadStockRequestItem {

	private Integer id;

	private Integer uploadedQuantity;

	private Long batchId;

	private Integer uomTypeId;

	private Integer uomUnitId;

	private Integer quantity;

	private Integer statusId;

	private Boolean isApproved;

	private Integer rejectReasonId;

	private String rejectNote;

	private Integer computeStockDetailId;

	private Integer varianceApprovalDetailId;
	private Integer varianceQty; 
	private Double varianceCop; 
	private Double varianceMrp;
	public Integer getId() {
		return id;
	}

	public Integer getVarianceQty() {
		return varianceQty;
	}

	public void setVarianceQty(Integer varianceQty) {
		this.varianceQty = varianceQty;
	}

	public Double getVarianceCop() {
		return varianceCop;
	}

	public void setVarianceCop(Double varianceCop) {
		this.varianceCop = varianceCop;
	}

	public Double getVarianceMrp() {
		return varianceMrp;
	}

	public void setVarianceMrp(Double varianceMrp) {
		this.varianceMrp = varianceMrp;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUploadedQuantity() {
		return uploadedQuantity;
	}

	public void setUploadedQuantity(Integer uploadedQuantity) {
		this.uploadedQuantity = uploadedQuantity;
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

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
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

	public Integer getComputeStockDetailId() {
		return computeStockDetailId;
	}

	public void setComputeStockDetailId(Integer computeStockDetailId) {
		this.computeStockDetailId = computeStockDetailId;
	}

	public Integer getVarianceApprovalDetailId() {
		return varianceApprovalDetailId;
	}

	public void setVarianceApprovalDetailId(Integer varianceApprovalDetailId) {
		this.varianceApprovalDetailId = varianceApprovalDetailId;
	}

}
