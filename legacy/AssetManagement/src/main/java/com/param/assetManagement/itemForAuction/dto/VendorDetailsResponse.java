package com.param.assetManagement.itemForAuction.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class VendorDetailsResponse extends VendorDetails {
	
	protected Integer auctionQuantity;
	
	protected Integer approvedAuctionQuantity;
	
	public Integer getAuctionQuantity() {
		return auctionQuantity;
	}

	public void setAuctionQuantity(Integer auctionQuantity) {
		this.auctionQuantity = auctionQuantity;
	}

	public Integer getApprovedAuctionQuantity() {
		return approvedAuctionQuantity;
	}

	public void setApprovedAuctionQuantity(Integer approvedAuctionQuantity) {
		this.approvedAuctionQuantity = approvedAuctionQuantity;
	}

	public VendorDetailsResponse(Integer vendorId, String vendorName) {
		super();
		this.vendorId = vendorId;
		this.vendorName = vendorName;
	}
	
	public VendorDetailsResponse(Integer id, Integer auctionQuantity, Integer approvedAuctionQuantity, Integer vendorId,
			String vendorName, Double vendorAmount, String paymentTerms, String referenceNumber, Date referenceDate) {
		super();
		this.id = id;
		this.auctionQuantity = auctionQuantity;
		this.approvedAuctionQuantity = approvedAuctionQuantity;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.vendorAmount = vendorAmount;
		this.paymentTerms = paymentTerms;
		this.referenceNumber = referenceNumber;
		this.referenceDate = referenceDate;
	}

	
}