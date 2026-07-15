package com.param.inventory.store.dto.drugRecall;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class DrugRecallRequest extends CookieObjectDto{
	
	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected Integer requestTypeId;

	protected Integer statusId;

	protected String recallNo;

	protected Integer cancelReasonId;

	protected String cancelNote;
	
	private Integer approvalStatusId;

    private List<Integer> drugRecallId;

	protected List<DrugRecallItemRequest> items;
	
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

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public String getRecallNo() {
		return recallNo;
	}

	public void setRecallNo(String recallNo) {
		this.recallNo = recallNo;
	}

	public List<DrugRecallItemRequest> getItems() {
		return items;
	}

	public void setItems(List<DrugRecallItemRequest> items) {
		this.items = items;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<Integer> getDrugRecallId() {
		return drugRecallId;
	}

	public void setDrugRecallId(List<Integer> drugRecallId) {
		this.drugRecallId = drugRecallId;
	}
	
}
