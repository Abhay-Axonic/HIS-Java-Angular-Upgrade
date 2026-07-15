package com.param.inventory.store.dto.drugRecall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DrugRecallResponse {

	protected Integer id;

	protected Date recallDate;

	protected String recallNo;

	protected Integer requestTypeId;

	protected String requestType;

	protected String requestedBy;

	protected String status;

	protected Integer statusId;

	protected List<DrugRecallItemResponse> items;

	public DrugRecallResponse() {
		super();
	}

	public List<DrugRecallItemResponse> getItems() {
		return items;
	}

	public void setItems(List<DrugRecallItemResponse> items) {
		this.items = items;
	}

	public DrugRecallResponse(Integer id, String recallNo, Date addedDate, String requestedBy, Integer requestTypeId,
			String requestType, Integer statusId, String status) {

		this.id = id;
		this.recallNo = recallNo;
		this.recallDate = addedDate;
		this.requestedBy = requestedBy;
		this.requestTypeId = requestTypeId;
		this.requestType = requestType;
		this.statusId = statusId;
		this.status = status;

	}

	public void addItems(DrugRecallItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<DrugRecallItemResponse>();

		this.items.add(item);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getRecallDate() {
		return recallDate;
	}

	public void setRecallDate(Date recallDate) {
		this.recallDate = recallDate;
	}

	public String getRecallNo() {
		return recallNo;
	}

	public void setRecallNo(String recallNo) {
		this.recallNo = recallNo;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

}
