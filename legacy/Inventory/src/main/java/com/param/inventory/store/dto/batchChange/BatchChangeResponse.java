package com.param.inventory.store.dto.batchChange;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BatchChangeResponse {
	
	protected Integer id;
	
	protected String adjustmentNo;
	
	protected Date adjustmentDate;
	
	protected Integer storeId;
	
	protected String storeName;
	
	protected String adjustmentBy;
	
	protected Integer statusId;
	
	protected String status;
	
	protected Integer approvalStatusId;
	
	protected String approvalStatus;
	
	protected List<BatchResponse> batch;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdjustmentNo() {
		return adjustmentNo;
	}

	public void setAdjustmentNo(String adjustmentNo) {
		this.adjustmentNo = adjustmentNo;
	}

	public Date getAdjustmentDate() {
		return adjustmentDate;
	}

	public void setAdjustmentDate(Date adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAdjustmentBy() {
		return adjustmentBy;
	}

	public void setAdjustmentBy(String adjustmentBy) {
		this.adjustmentBy = adjustmentBy;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}
}