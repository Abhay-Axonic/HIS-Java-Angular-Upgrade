package com.param.assetManagement.auctionRequisition.dto;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class AuctionRequisitionApprovalRequest extends CookieObjectDto{

	protected Integer id;
	
	protected Integer approvalStatusId;
	
	protected Integer statusId;
	
	List<ItemAuctionRequisitionApproveRequest> items;
	
	List<Integer> auctionRequisitionId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<ItemAuctionRequisitionApproveRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemAuctionRequisitionApproveRequest> items) {
		this.items = items;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<Integer> getAuctionRequisitionId() {
		return auctionRequisitionId;
	}

	public void setAuctionRequisitionId(List<Integer> auctionRequisitionId) {
		this.auctionRequisitionId = auctionRequisitionId;
	}
	
}
