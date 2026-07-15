package com.param.inventory.store.dto.rejectMaterialAcceptance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueItemResponse;

@JsonInclude(Include.NON_NULL)
public class RejectMaterialResponse {

	protected Integer id;

	protected Date returnDate;

	protected String returnNo;

	protected Integer fromStoreId;

	protected String fromStore;

	protected Integer toStoreId;

	protected String toStore;

	protected String indentType;

	protected String returnBy;

	protected Boolean isConsignment;

	protected String remark;

	protected String status;

	protected Integer statusId;

	protected Integer materialIssueId;
	
	protected Integer returnId;

	protected String issueNo;

	protected List<MaterialIssueItemResponse> items;

	public RejectMaterialResponse() {
		super();
	}

	public RejectMaterialResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy, Boolean isConsignment, String remark, String status,
			Integer statusId, Integer materialIssueId) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.returnBy = returnBy;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.status = status;
		this.statusId = statusId;
		this.materialIssueId = materialIssueId;

	}
	
	public RejectMaterialResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy, Boolean isConsignment,
			Integer statusId, Integer returnId, String remark, String status) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.returnBy = returnBy;
		this.isConsignment = isConsignment;
		this.statusId = statusId;
		this.returnId = returnId;
		this.remark = remark;
		this.status = status;
	}

	public Integer getReturnId() {
		return returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public String getFromStore() {
		return fromStore;
	}

	public void setFromStore(String fromStore) {
		this.fromStore = fromStore;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}

	public String getReturnBy() {
		return returnBy;
	}

	public void setReturnBy(String returnBy) {
		this.returnBy = returnBy;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void addItems(MaterialIssueItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<MaterialIssueItemResponse>();

		this.items.add(item);
	}

	public List<MaterialIssueItemResponse> getItems() {
		return items;
	}

	public void setItems(List<MaterialIssueItemResponse> items) {
		this.items = items;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getMaterialIssueId() {
		return materialIssueId;
	}

	public void setMaterialIssueId(Integer materialIssueId) {
		this.materialIssueId = materialIssueId;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
}
