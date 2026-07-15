package com.param.inventory.store.dto.batchChange;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class BatchChangeRequest extends CookieObjectDto{
	
	protected Integer id;
	
	protected Date fromDate;
	
	protected Date toDate;
	
	protected Integer storeId;
	
	protected Integer statusId;
	
	protected Integer approvalStatusId;

	protected Integer cancelReasonId;

	protected String cancelNote;
	
	protected List<Integer> adjustmentId;
	
	protected List<BatchRequest> batch;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public List<BatchRequest> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchRequest> batch) {
		this.batch = batch;
	}

	public List<Integer> getAdjustmentId() {
		return adjustmentId;
	}

	public void setAdjustmentId(List<Integer> adjustmentId) {
		this.adjustmentId = adjustmentId;
	}
}
