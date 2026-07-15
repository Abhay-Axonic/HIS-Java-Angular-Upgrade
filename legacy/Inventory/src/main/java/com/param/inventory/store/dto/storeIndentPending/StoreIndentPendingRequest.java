package com.param.inventory.store.dto.storeIndentPending;

import java.util.Date;
import java.util.List;

public class StoreIndentPendingRequest {

	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected Integer fromStoreId;

	protected Integer toStoreId;

	protected String indentNo;

	protected Integer statusId;

	protected Integer indentTypeId;

	protected Integer priorityId;

	protected Boolean isConsignment;

	protected String remark;

	protected Integer approvalStatusId;

	protected List<ItemPendingRequest> items;

	protected List<Integer> indentId;

	protected Integer closeReasonId;

	protected String closeNote;
	
	protected Integer userId;

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

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getIndentTypeId() {
		return indentTypeId;
	}

	public void setIndentTypeId(Integer indentTypeId) {
		this.indentTypeId = indentTypeId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<ItemPendingRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemPendingRequest> items) {
		this.items = items;
	}

	public List<Integer> getIndentId() {
		return indentId;
	}

	public void setIndentId(List<Integer> indentId) {
		this.indentId = indentId;
	}

	public Integer getCloseReasonId() {
		return closeReasonId;
	}

	public void setCloseReasonId(Integer closeReasonId) {
		this.closeReasonId = closeReasonId;
	}

	public String getCloseNote() {
		return closeNote;
	}

	public void setCloseNote(String closeNote) {
		this.closeNote = closeNote;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/*
	 * public Integer getCancelReasonId() { return cancelReasonId; }
	 * 
	 * public void setCancelReasonId(Integer cancelReasonId) { this.cancelReasonId =
	 * cancelReasonId; }
	 * 
	 * public String getCancelNote() { return cancelNote; }
	 * 
	 * public void setCancelNote(String cancelNote) { this.cancelNote = cancelNote;
	 * }
	 */

}
