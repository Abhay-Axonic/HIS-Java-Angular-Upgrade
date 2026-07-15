package com.param.pharmacy.nursing.workstation.dto.drugAdministration;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class DoseTime1
{
	 private Integer id; 
	 
	 private Integer drugAdministrationId;
	
	 private String administrationTime;
	 
	 private String preBufferAdministrationTime;
	 
	 private String postBufferAdministrationTime;
	 
	 private String actualAdministrationTime;
	 
	 private String batchNo; 
	 
	 private Integer batchId; 
	 
	private Integer reasonId;
		 
    private String reason;
		 
	private String note;
	private Integer orderQty;

	 public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDrugAdministrationId() {
		return drugAdministrationId;
	}

	public void setDrugAdministrationId(Integer drugAdministrationId) {
		this.drugAdministrationId = drugAdministrationId;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public String getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(String preBufferAdministrationTime) {
		this.preBufferAdministrationTime = preBufferAdministrationTime;
	}

	public String getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(String postBufferAdministrationTime) {
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	public String getActualAdministrationTime() {
		return actualAdministrationTime;
	}

	public void setActualAdministrationTime(String actualAdministrationTime) {
		this.actualAdministrationTime = actualAdministrationTime;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

		 
}