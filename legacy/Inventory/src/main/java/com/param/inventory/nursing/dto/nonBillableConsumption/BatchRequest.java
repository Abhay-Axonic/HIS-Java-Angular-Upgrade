package com.param.inventory.nursing.dto.nonBillableConsumption;

import java.util.Date;

public class BatchRequest {
	protected Integer id;

	protected Long batchId;
	
	protected Integer uomTypeId;
	
	protected Integer uomUnitId;
	
	protected String batchNo;
	
	protected Date expiry;
	
	protected Integer consumedQuantity;
	
	protected Integer acceptedQuantity;
	
	protected Integer rejectedQuantity;
	
	protected Integer statusId;

	protected Integer rejectReasonId;

	protected String rejectNote;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Integer getConsumedQuantity() {
		return consumedQuantity;
	}

	public void setConsumedQuantity(Integer consumedQuantity) {
		this.consumedQuantity = consumedQuantity;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectQuantity) {
		this.rejectedQuantity = rejectQuantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}
}
