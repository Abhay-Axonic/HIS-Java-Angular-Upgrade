package com.param.inventory.store.dto.batchChange;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BatchRequest {
	protected Integer id;

	protected Long batchId;

	protected String oldBatchNo;

	protected Date oldBatchExpiry;

	protected Double oldMrp;

	protected String newBatchNo;

	protected Date newBatchExpiry;

	protected Double newMrp;
	protected Double lcop;
	protected Double cop;

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	protected Integer statusId;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Integer mrpUomTypeId;

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

	public String getOldBatchNo() {
		return oldBatchNo;
	}

	public void setOldBatchNo(String oldBatchNo) {
		this.oldBatchNo = oldBatchNo;
	}

	public Date getOldBatchExpiry() {
		return oldBatchExpiry;
	}

	public void setOldBatchExpiry(Date oldBatchExpiry) {
		this.oldBatchExpiry = oldBatchExpiry;
	}

	public Double getOldMrp() {
		return oldMrp;
	}

	public void setOldMrp(Double oldMrp) {
		this.oldMrp = oldMrp;
	}

	public String getNewBatchNo() {
		return newBatchNo;
	}

	public void setNewBatchNo(String newBatchNo) {
		this.newBatchNo = newBatchNo;
	}

	public Date getNewBatchExpiry() {
		return newBatchExpiry;
	}

	public void setNewBatchExpiry(Date newBatchExpiry) {
		this.newBatchExpiry = newBatchExpiry;
	}

	public Double getNewMrp() {
		return newMrp;
	}

	public void setNewMrp(Double newMrp) {
		this.newMrp = newMrp;
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

	public Integer getMrpUomTypeId() {
		return mrpUomTypeId;
	}

	public void setMrpUomTypeId(Integer mrpUomTypeId) {
		this.mrpUomTypeId = mrpUomTypeId;
	}

}