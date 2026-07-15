package com.param.procurement.quotation.dto.vendorSelection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationDetailsResponse;

public class VendorSelectionResponse {

	private Integer id;

	private Date qrDate;

	private String qrNumber;

	private String storeName;

	private String assetType;

	private String qrAgainst;

	private String requestedBy;

	private Date requestedDate;

	private String status;
	
	private List<VendorSelectionDetailResponse> items;

	public VendorSelectionResponse() {
		super();
	}

	public VendorSelectionResponse(Integer id, Date qrDate, String qrNumber, String storeName, String assetType,
			String qrAgainst, String requestedBy, Date requestedDate, String status) {
		super();
		this.id = id;
		this.qrDate = qrDate;
		this.qrNumber = qrNumber;
		this.storeName = storeName;
		this.assetType = assetType;
		this.qrAgainst = qrAgainst;
		this.requestedBy = requestedBy;
		this.requestedDate = requestedDate;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getQrAgainst() {
		return qrAgainst;
	}

	public void setQrAgainst(String qrAgainst) {
		this.qrAgainst = qrAgainst;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<VendorSelectionDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<VendorSelectionDetailResponse> items) {
		this.items = items;
	}
	
	
	public void addItems(VendorSelectionDetailResponse item) {
		if (this.items == null)
			this.items = new ArrayList<VendorSelectionDetailResponse>();
		
		this.items.add(item);
	}
}
