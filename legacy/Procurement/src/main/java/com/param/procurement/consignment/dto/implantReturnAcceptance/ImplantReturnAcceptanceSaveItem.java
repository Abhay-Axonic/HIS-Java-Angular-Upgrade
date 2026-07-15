package com.param.procurement.consignment.dto.implantReturnAcceptance;

public class ImplantReturnAcceptanceSaveItem
{
	private Integer id;
	
	 private Integer batchMapperId;
	
	private Integer issueDetailBatchMapper;
	
	private Integer acceptQuantity;
	
	private Integer issueRejectedQuantity;
	
	private Integer rejectedQuantity;
	
	private Integer rejectReason;
	
	private String rejectedNote;
	
	private Integer statusId;
		   
	private String status;
	
	private Integer reasonId;
	 
     private String note;
     
     private Integer batchMapper;

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
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
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

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getBatchMapper() {
		return batchMapper;
	}

	public void setBatchMapper(Integer batchMapper) {
		this.batchMapper = batchMapper;
	}

	public Integer getIssueRejectedQuantity() {
		return issueRejectedQuantity;
	}

	public void setIssueRejectedQuantity(Integer issueRejectedQuantity) {
		this.issueRejectedQuantity = issueRejectedQuantity;
	}
	
}