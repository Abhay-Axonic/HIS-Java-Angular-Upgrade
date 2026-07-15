package com.param.assetManagement.auctionRequisition.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemAuctionRequisitionApproveRequest {

	protected Integer id;

	protected Integer acceptedAuctionQuantiity;

	protected Double auctionPrice;

	protected Double totalAuctionPrice;

	protected Integer rejectAuctionQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected Integer statusId;

	protected List<ItemAuctionRequisitionAccessoriesRequest> accessories;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAcceptedAuctionQuantiity() {
		return acceptedAuctionQuantiity;
	}

	public void setAcceptedAuctionQuantiity(Integer acceptedAuctionQuantiity) {
		this.acceptedAuctionQuantiity = acceptedAuctionQuantiity;
	}

	public Double getAuctionPrice() {
		return auctionPrice;
	}

	public void setAuctionPrice(Double auctionPrice) {
		this.auctionPrice = auctionPrice;
	}

	public Double getTotalAuctionPrice() {
		return totalAuctionPrice;
	}

	public void setTotalAuctionPrice(Double totalAuctionPrice) {
		this.totalAuctionPrice = totalAuctionPrice;
	}

	public Integer getRejectAuctionQuantity() {
		return rejectAuctionQuantity;
	}

	public void setRejectAuctionQuantity(Integer rejectAuctionQuantity) {
		this.rejectAuctionQuantity = rejectAuctionQuantity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public List<ItemAuctionRequisitionAccessoriesRequest> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<ItemAuctionRequisitionAccessoriesRequest> accessories) {
		this.accessories = accessories;
	}

}
