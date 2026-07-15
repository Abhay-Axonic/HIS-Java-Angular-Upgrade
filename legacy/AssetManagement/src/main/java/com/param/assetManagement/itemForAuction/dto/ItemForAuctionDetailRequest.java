package com.param.assetManagement.itemForAuction.dto;

import java.util.List;

public class ItemForAuctionDetailRequest {
	
	protected Integer id;
	
	protected Integer statusId;
	
	protected List<VendorDetails> vendorDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<VendorDetails> getVendorDetails() {
		return vendorDetails;
	}

	public void setVendorDetails(List<VendorDetails> vendorDetails) {
		this.vendorDetails = vendorDetails;
	}
	
}