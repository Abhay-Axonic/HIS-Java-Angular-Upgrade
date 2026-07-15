package com.param.inventory.store.dto.materialIssueAcceptance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueItemResponse;

@JsonInclude(Include.NON_NULL)
public class MaterialIssueResponse {

	protected Integer id;

	protected Date issueDate;

	protected String issueNo;

	protected String indentNo;

	protected Integer fromStoreId;

	protected String fromStore;

	protected Integer toStoreId;

	protected String toStore;

	protected Integer priorityId;

	protected String priority;

	protected String indentType;

	protected Boolean isConsignment;

	protected String remark;
	
	protected Integer statusId;

	protected String status;
	
	protected String statusNew;

	protected List<MaterialIssueItemResponse> items;

	public MaterialIssueResponse() {
		super();
	}

	public MaterialIssueResponse(Integer id, Date issueDate, String issueNo, String indentNo, Integer fromStoreId,
			String fromStore, Integer toStoreId, String toStore, String priority, String indentType,
			Boolean isConsignment, String remark) {
		super();
		this.id = id;
		this.issueDate = issueDate;
		this.issueNo = issueNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.indentType = indentType;
		this.isConsignment = isConsignment;
		this.remark = remark;
	}
	
	public MaterialIssueResponse(Integer id, Date issueDate, String issueNo, String indentNo, Integer fromStoreId,
			String fromStore, Integer toStoreId, String toStore, String priority, String indentType,
			Boolean isConsignment, String remark, Integer statusId, String status) {
		super();
		this.id = id;
		this.issueDate = issueDate;
		this.issueNo = issueNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.indentType = indentType;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
	}

	public MaterialIssueResponse(Integer id, Date issueDate, String issueNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String priority, String indentType, Boolean isConsignment, String remark,
			String status) {
		super();
		this.id = id;
		this.issueDate = issueDate;
		this.issueNo = issueNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.indentType = indentType;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
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

	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<MaterialIssueItemResponse> getItems() {
		return items;
	}

	public void setItems(List<MaterialIssueItemResponse> items) {
		this.items = items;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public void addItems(MaterialIssueItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<MaterialIssueItemResponse>();

		this.items.add(item);
	}

	public String getStatusNew() {
		return statusNew;
	}

	public void setStatusNew(String statusNew) {
		this.statusNew = statusNew;
	}

}
