package com.param.inventory.store.dto.storeIndentPending;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreIndentPendingResponse {

	protected Integer id;

	protected Date indentDate;

	protected String indentNo;

	protected String fromStore;

	protected String toStore;

	protected String priority;

	protected String type;

	protected Boolean isConsignment;

	protected String requestedBy;

	protected String remark;

	protected String status;

	protected Integer statusId;

	protected String approvalStatus;

	protected Integer approvalStatusId;

	protected List<ItemPendingResponse> items;

	public StoreIndentPendingResponse() {
		super();
	}

	public StoreIndentPendingResponse(Integer id, Date indentDate, String indentNo, String fromStore, String toStore,
			String priority, String type, Boolean isConsignment, String requestedBy, String remark, String status,
			Integer statusId, String approvalStatus, Integer approvalStatusId) {
		super();
		this.id = id;
		this.indentDate = indentDate;
		this.indentNo = indentNo;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.priority = priority;
		this.type = type;
		this.isConsignment = isConsignment;
		this.requestedBy = requestedBy;
		this.remark = remark;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public String getFromStore() {
		return fromStore;
	}

	public void setFromStore(String fromStore) {
		this.fromStore = fromStore;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<ItemPendingResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemPendingResponse> items) {
		this.items = items;
	}

	public void addItems(ItemPendingResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ItemPendingResponse>();

		this.items.add(item);
	}

}
