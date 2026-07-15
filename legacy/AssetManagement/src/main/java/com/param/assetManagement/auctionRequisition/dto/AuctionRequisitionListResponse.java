package com.param.assetManagement.auctionRequisition.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.assetManagement.auctionRequisition.dto.RejectReasonResponse;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class AuctionRequisitionListResponse extends CookieObjectDto{

	protected Integer id;
	
	protected String requisitionNo;
	
	protected Date requisitionDate;
	
	protected Integer requisitionBy;
	
	protected String requisitionName;
	
	protected String store;
	
	protected Integer storeId;
	
	protected Integer statusId;
	
	protected String status;

	protected String remark;
	
	protected Integer approvalStatusId;
	
	protected String approvalStatus;
	
	protected List<Integer> auctionRequisitionId;
	
	protected Integer cancelReasonId;
	
	protected String cancelNote;
	
	protected List<ItemAuctionRequisitionResponse> items;
	
	protected List<RejectReasonResponse> rejectReasons;
	
	public AuctionRequisitionListResponse() {
		super();
	}

	public AuctionRequisitionListResponse(Integer id, String requisitionNo, Date requisitionDate, Integer requisitionBy,
			String requisitionName, String store, Integer storeId, Integer statusId, String status) {
		super();
		this.id = id;
		this.requisitionNo = requisitionNo;
		this.requisitionDate = requisitionDate;
		this.requisitionBy = requisitionBy;
		this.requisitionName = requisitionName;
		this.store = store;
		this.storeId = storeId;
		this.statusId = statusId;
		this.status = status;
	}
	
	public AuctionRequisitionListResponse(Integer id, String requisitionNo, Date requisitionDate, Integer requisitionBy,
			String requisitionName, String store, Integer storeId, Integer statusId, String status, List<RejectReasonResponse> rejectReasons) {
		super();
		this.id = id;
		this.requisitionNo = requisitionNo;
		this.requisitionDate = requisitionDate;
		this.requisitionBy = requisitionBy;
		this.requisitionName = requisitionName;
		this.store = store;
		this.storeId = storeId;
		this.statusId = statusId;
		this.status = status;
		this.rejectReasons = rejectReasons;
	}

	public List<ItemAuctionRequisitionResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemAuctionRequisitionResponse> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequisitionNo() {
		return requisitionNo;
	}

	public void setRequisitionNo(String requisitionNo) {
		this.requisitionNo = requisitionNo;
	}

	public Date getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(Date requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public Integer getRequisitionBy() {
		return requisitionBy;
	}

	public void setRequisitionBy(Integer requisitionBy) {
		this.requisitionBy = requisitionBy;
	}

	public String getRequisitionName() {
		return requisitionName;
	}

	public void setRequisitionName(String requisitionName) {
		this.requisitionName = requisitionName;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void addItems(ItemAuctionRequisitionResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ItemAuctionRequisitionResponse>();

		this.items.add(item);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<Integer> getAuctionRequisitionId() {
		return auctionRequisitionId;
	}

	public void setAuctionRequisitionId(List<Integer> auctionRequisitionId) {
		this.auctionRequisitionId = auctionRequisitionId;
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

	public List<RejectReasonResponse> getRejectReasons() {
		return rejectReasons;
	}

	public void setRejectReasons(List<RejectReasonResponse> rejectReasons) {
		this.rejectReasons = rejectReasons;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
}
