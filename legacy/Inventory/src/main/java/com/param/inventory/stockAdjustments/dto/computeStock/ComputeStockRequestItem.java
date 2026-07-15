package com.param.inventory.stockAdjustments.dto.computeStock;

import java.util.Date;
import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.inventory.stockAdjustments.dto.varianceApproval.BatchResponse;

public class ComputeStockRequestItem {

	protected Integer id;
	
	protected String batchNo;
	
	protected Date expiryDate;
	
	protected Double mrp;
	
	protected String newBatchNo;
	
	protected Date newExpiryDate;
	
	protected Double newMrp;
	
	protected Integer approvedQuantity;
	
	protected Integer rejectQuantity;
	
	protected Integer rejectReasonId;
	
	protected String rejectNote;
	
	protected String remark;

	protected Double varianceAtCOP;
	
	protected Double varianceAtMrp;
	
	protected Date newExpiry;
	
	protected Double cop;
	
	protected List<BatchResponse> batches;
	
	protected Integer itemId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public String getNewBatchNo() {
		return newBatchNo;
	}

	public void setNewBatchNo(String newBatchNo) {
		this.newBatchNo = newBatchNo;
	}

	public Date getNewExpiryDate() {
		return newExpiryDate;
	}

	public void setNewExpiryDate(Date newExpiryDate) {
		this.newExpiryDate = newExpiryDate;
	}

	public Double getNewMrp() {
		return newMrp;
	}

	public void setNewMrp(Double newMrp) {
		this.newMrp = newMrp;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}	

	public Double getVarianceAtCOP() {
		return varianceAtCOP;
	}

	public void setVarianceAtCOP(Double varianceAtCOP) {
		this.varianceAtCOP = varianceAtCOP;
	}
	
	public Double getVarianceAtMrp() {
		return varianceAtMrp;
	}

	public void setvarianceAtMrp(Double varianceAtMrp) {
		this.varianceAtMrp = varianceAtMrp;
	}
	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public List<BatchResponse> getBatches() {
		return batches;
	}

	public void setBatches(List<BatchResponse> batches) {
		this.batches = batches;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	
}