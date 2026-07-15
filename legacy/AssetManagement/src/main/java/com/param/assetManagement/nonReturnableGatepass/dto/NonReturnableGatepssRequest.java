package com.param.assetManagement.nonReturnableGatepass.dto;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class NonReturnableGatepssRequest extends CookieObjectDto{

	protected Integer id;
	
	protected List<Integer> ids;

	protected Date fromDate;

	protected Date toDate;

	protected Integer storeId;

	protected Integer statusId;
	
	protected Integer approvalStatusId;

	protected Integer cancelReasonId;

	protected String cancelNote;
	
	protected Integer vendorId;
	
	protected Integer requestTypeId;
	
	protected Integer auctionRequisitionId;
	
	protected String nonReturnableGatepassNo;

	protected String requestNo;
	
	protected List<NonReturnableGatepssRequestItem> items;
	
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getNonReturnableGatepassNo() {
		return nonReturnableGatepassNo;
	}

	public void setNonReturnableGatepassNo(String nonReturnableGatepassNo) {
		this.nonReturnableGatepassNo = nonReturnableGatepassNo;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getAuctionRequisitionId() {
		return auctionRequisitionId;
	}

	public void setAuctionRequisitionId(Integer auctionRequisitionId) {
		this.auctionRequisitionId = auctionRequisitionId;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public List<NonReturnableGatepssRequestItem> getItems() {
		return items;
	}

	public void setItems(List<NonReturnableGatepssRequestItem> items) {
		this.items = items;
	}
	
}