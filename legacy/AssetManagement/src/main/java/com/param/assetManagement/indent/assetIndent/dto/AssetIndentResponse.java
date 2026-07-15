package com.param.assetManagement.indent.assetIndent.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AssetIndentResponse {

	protected Integer id;

	protected Date indentDate;

	protected String indentNo;

	protected Integer fromStoreId;

	protected String fromStore;

	protected Integer toStoreId;

	protected String toStore;

	protected Integer priorityId;

	protected String priority;

	protected String type;

	protected String requestedBy;

	protected String remark;

	protected String status;

	protected Integer statusId;

	protected String approvalStatus;

	protected Integer approvalStatusId;

	protected List<AssetIndentResponseItem> items;

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

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public String getFromStore() {
		return fromStore;
	}

	public void setFromStore(String fromStore) {
		this.fromStore = fromStore;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
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

	public List<AssetIndentResponseItem> getItems() {
		return items;
	}

	public void setItems(List<AssetIndentResponseItem> items) {
		this.items = items;
	}

	public AssetIndentResponse() {
		super();
	}

	public AssetIndentResponse(Integer id, Date indentDate, String indentNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, Integer priorityId, String priority, String type, String requestedBy,
			String remark, String status, Integer statusId, String approvalStatus, Integer approvalStatusId) {
		super();
		this.id = id;
		this.indentDate = indentDate;
		this.indentNo = indentNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priorityId = priorityId;
		this.priority = priority;
		this.type = type;
		this.requestedBy = requestedBy;
		this.remark = remark;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
	}

	public AssetIndentResponse(Integer id, Integer fromStoreId, String fromStore, Integer toStoreId, String toStore,
			String remark, String status) {
		super();
		this.id = id;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.remark = remark;
		this.status = status;
	}

	public AssetIndentResponse(Integer id, Integer fromStoreId, String fromStore, Integer toStoreId, String toStore,
			String remark, String status, Integer statusId) {
		this(id, fromStoreId, fromStore, toStoreId, toStore, remark, status);
		this.statusId = statusId;
	}

	public AssetIndentResponse(Integer id, String indentNo, Integer fromStoreId, String fromStore, Integer toStoreId,
			String toStore, String priority, String type, String remark, String status, Integer statusId) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.type = type;
		this.remark = remark;
		this.status = status;
		this.statusId = statusId;
	}

	public AssetIndentResponse(Integer id, String indentNo, Integer fromStoreId, String fromStore, Integer toStoreId,
			String toStore, String priority, String type, String remark) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.type = type;
		this.remark = remark;
	}

	public AssetIndentResponse(Integer id, Integer fromStoreId, String fromStore, Integer toStoreId, String toStore,
			String priority, String type, String remark) {
		super();
		this.id = id;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.type = type;
		this.remark = remark;
	}

}