package com.param.assetManagement.itemForAuction.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class VendorDetails {
	
	protected Integer vendorId;
	
	protected String vendorName;
	
    protected Double vendorAmount;
   
    protected String paymentTerms;
    
    protected String referenceNumber;
    
    protected Date referenceDate;
    
    protected Integer id;
    
    protected Integer approvedAuctionQuantity;
    
	protected Boolean isAccepted;
	
	protected String rejectNote;

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Double getVendorAmount() {
		return vendorAmount;
	}

	public void setVendorAmount(Double vendorAmount) {
		this.vendorAmount = vendorAmount;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovedAuctionQuantity() {
		return approvedAuctionQuantity;
	}

	public void setApprovedAuctionQuantity(Integer approvedAuctionQuantity) {
		this.approvedAuctionQuantity = approvedAuctionQuantity;
	}

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}
    
}