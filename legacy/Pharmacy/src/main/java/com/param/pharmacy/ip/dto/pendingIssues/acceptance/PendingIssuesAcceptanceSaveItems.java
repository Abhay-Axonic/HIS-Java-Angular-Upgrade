package com.param.pharmacy.ip.dto.pendingIssues.acceptance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PendingIssuesAcceptanceSaveItems
{
	 private Integer id;
	 
	 private Integer batchMapperId;
	 
	 private Integer acceptQuantity;
	 
	 private Integer rejectQuantity;
	 private Integer  pharmReasonId;
	 
	

	public Integer getPharmReasonId() {
		return pharmReasonId;
	}

	public void setPharmReasonId(Integer pharmReasonId) {
		this.pharmReasonId = pharmReasonId;
	}

	private Integer reasonId;
	 
     private String note;
     
     private Integer statusId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Integer acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
     
}