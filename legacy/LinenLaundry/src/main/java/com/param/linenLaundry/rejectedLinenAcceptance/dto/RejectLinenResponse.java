package com.param.linenLaundry.rejectedLinenAcceptance.dto;

import java.util.Date;

public class RejectLinenResponse {

	protected Integer id;

	protected Date returnDate;

	protected String returnNo;

	protected Integer fromStoreId;

	protected String fromStore;

	protected Integer toStoreId;

	protected String toStore;

	protected String indentType;

	protected String returnBy;

	protected String remark;
	

	protected String status;

	protected Integer statusId;

	protected Integer linenIssueId;
	
	protected Integer returnId;

	//protected List<LinenIssueItemResponse> items;

	public RejectLinenResponse() {
		super();
	}

	public RejectLinenResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy, String remark, String status,
			Integer statusId, Integer linenIssueId) {
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
		this.statusId = statusId;
		this.linenIssueId = linenIssueId;
	}
	
	public RejectLinenResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy,
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
		this.statusId = statusId;
		this.returnId = returnId;
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

	public Integer getMaterialIssueId() {
		return linenIssueId;
	}

	public void setMaterialIssueId(Integer linenIssueId) {
		this.linenIssueId = linenIssueId;
	}

	public Integer getReturnId() {
		return returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}

}
