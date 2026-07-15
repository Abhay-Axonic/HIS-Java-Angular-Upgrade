package com.param.inventory.stockAdjustments.dto.computeStock;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ComputeStockRequest extends CookieObjectDto {

	protected Integer id;
	
	protected Integer storeId;

	protected Integer statusId;

	protected Date fromDate;

	protected Date toDate;
	
	protected Integer cancelReasonId;

	protected String cancelNote;

	protected Integer approvalStatusId;

	protected List<Integer> computeStockId;
	
	protected List<ComputeStockRequestItem> items;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<Integer> getComputeStockId() {
		return computeStockId;
	}

	public void setComputeStockId(List<Integer> computeStockId) {
		this.computeStockId = computeStockId;
	}

	public List<ComputeStockRequestItem> getItems() {
		return items;
	}

	public void setItems(List<ComputeStockRequestItem> items) {
		this.items = items;
	}

}
