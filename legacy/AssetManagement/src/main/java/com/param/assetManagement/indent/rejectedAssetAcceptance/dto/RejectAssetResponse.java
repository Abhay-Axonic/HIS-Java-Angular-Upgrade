package com.param.assetManagement.indent.rejectedAssetAcceptance.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.assetManagement.indent.assetIssueList.dto.AssetIssueItemResponse;

@JsonInclude(Include.NON_NULL)
public class RejectAssetResponse {

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

	protected List<AssetIssueItemResponse> items;

	public RejectAssetResponse() {
		super();
	}

	public RejectAssetResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy, String remark, String status) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.returnBy = returnBy;
		this.remark = remark;
		this.status = status;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<AssetIssueItemResponse> getItems() {
		return items;
	}

	public void setItems(List<AssetIssueItemResponse> items) {
		this.items = items;
	}

	public void addItems(AssetIssueItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<AssetIssueItemResponse>();

		this.items.add(item);
	}
}
