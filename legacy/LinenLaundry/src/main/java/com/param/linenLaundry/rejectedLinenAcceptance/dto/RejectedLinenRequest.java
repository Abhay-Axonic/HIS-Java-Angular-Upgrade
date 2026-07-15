package com.param.linenLaundry.rejectedLinenAcceptance.dto;

import java.util.Date;

public class RejectedLinenRequest {
	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected Integer fromStoreId;

	protected Integer toStoreId;

	protected Integer indentId;

	protected String indentNo;

	protected Integer indentTypeId;
	
	protected Integer statusId;

	protected Integer priorityId;

	protected String priority;

	protected Integer approvalStatusId;

	protected Integer closeReasonId;

	protected String closeNote;

	protected String remark;

	protected String issueNo;

	protected String returnNo;


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

	public Integer getIndentId() {
		return indentId;
	}

	public void setIndentId(Integer indentId) {
		this.indentId = indentId;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getIndentTypeId() {
		return indentTypeId;
	}

	public void setIndentTypeId(Integer indentTypeId) {
		this.indentTypeId = indentTypeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

}
