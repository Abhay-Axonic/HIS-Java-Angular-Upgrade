package com.param.inventory.nursing.dto.nonBillableConsumption;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ConsumptionRequest extends CookieObjectDto{
	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected Integer storeId;

	protected String consumptionNo;

	protected Integer statusId;

	protected Integer approvalStatusId;

	protected Integer cancelReasonId;

	protected String cancelNote;

	protected String remark;
	protected String requestFrom;
	protected Integer requestFromId;
	protected List<Integer> consumptionId;

	protected List<BatchRequest> batch;
	
	//protected String remark;

	public String getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}

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

	public String getConsumptionNo() {
		return consumptionNo;
	}

	public void setConsumptionNo(String consumptionNo) {
		this.consumptionNo = consumptionNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Integer> getConsumptionId() {
		return consumptionId;
	}

	public void setConsumptionId(List<Integer> consumptionId) {
		this.consumptionId = consumptionId;
	}

	public List<BatchRequest> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchRequest> batch) {
		this.batch = batch;
	}

	public Integer getRequestFromId() {
		return requestFromId;
	}

	public void setRequestFromId(Integer requestFromId) {
		this.requestFromId = requestFromId;
	}
}
