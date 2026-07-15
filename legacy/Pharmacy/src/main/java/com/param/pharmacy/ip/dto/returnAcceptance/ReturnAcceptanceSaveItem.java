package com.param.pharmacy.ip.dto.returnAcceptance;

public class ReturnAcceptanceSaveItem
{
	private Integer id;
	
	private Integer issueDetailBatchMapper;
	
	private Integer acceptQuantity;
	
	private Integer rejectedQuantity;

	private Integer returnQuantity;//added by Ganesh

	
	private Integer rejectReason;
	
	private String rejectedNote;
	
	private Integer statusId;
	private long batchId;


	public long getBatchId() {
		return batchId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIssueDetailBatchMapper() {
		return issueDetailBatchMapper;
	}

	public void setIssueDetailBatchMapper(Integer issueDetailBatchMapper) {
		this.issueDetailBatchMapper = issueDetailBatchMapper;
	}

	public Integer getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Integer acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity==null?0:rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity==null?0:rejectedQuantity;;
	}

	public Integer getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(Integer rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRejectedNote() {
		return rejectedNote;
	}

	public void setRejectedNote(String rejectedNote) {
		this.rejectedNote = rejectedNote;
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

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
}