package com.param.linenLaundry.rejectedLinenAcceptance.dto;

import java.util.Date;

public class RejectedLinenResponse {


	protected Integer id;

	protected Date indentDate;

	protected String indentNo;
	
	protected Date issueDate;
	
	protected String issueNo;

	protected Integer fromStoreId;

	protected String fromStore;

	protected Integer toStoreId;

	protected String toStore;

	protected Integer priorityId;

	protected String priority;

	protected String indentType;

	protected String remark;

	protected String status;
	
	protected String approvalStatus;

//	protected List<RejectedLinenDetailResponse> items;

public RejectedLinenResponse() {
	super();
}


public RejectedLinenResponse(Integer id, Date indentDate, String indentNo, Integer fromStoreId, String fromStore,
		Integer toStoreId, String toStore, String priority, String indentType) {
	super();
	this.id = id;
	this.indentDate = indentDate;
	this.indentNo = indentNo;
	this.fromStoreId = fromStoreId;
	this.fromStore = fromStore;
	this.toStoreId = toStoreId;
	this.toStore = toStore;
	this.priority = priority;
	this.indentType = indentType;
}

public RejectedLinenResponse(Integer id, Date indentDate, String indentNo, Integer fromStoreId, String fromStore,
		Integer toStoreId, String toStore, String priority, String indentType, String remark,
		String status) {
	super();
	this.id = id;
	this.indentDate = indentDate;
	this.indentNo = indentNo;
	this.fromStore = fromStore;
	this.toStore = toStore;
	this.priority = priority;
	this.indentType = indentType;
	this.remark = remark;
	this.status = status;
}

public RejectedLinenResponse(Integer id, String indentNo, Date issueDate, String issueNo, Integer fromStoreId,
		String fromStore, Integer toStoreId, String toStore, String priority, String indentType,
		String remark, String status) {
	super();
	this.id = id;
	this.indentNo = indentNo;
	this.issueDate = issueDate;
	this.issueNo = issueNo;
	this.fromStoreId = fromStoreId;
	this.fromStore = fromStore;
	this.toStoreId = toStoreId;
	this.toStore = toStore;
	this.priority = priority;
	this.indentType = indentType;
	this.remark = remark;
	this.status = status;
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

public String getApprovalStatus() {
	return approvalStatus;
}

public void setApprovalStatus(String approvalStatus) {
	this.approvalStatus = approvalStatus;
}

}

