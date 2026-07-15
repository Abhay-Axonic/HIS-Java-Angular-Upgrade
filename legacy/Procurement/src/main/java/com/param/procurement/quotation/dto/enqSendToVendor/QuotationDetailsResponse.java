package com.param.procurement.quotation.dto.enqSendToVendor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuotationDetailsResponse {
	
	private Integer id;
	
	private String qrNumber;
	
	private Date qrDate;
	
	private Integer assetTypeId;
	
	private String assetType;
	
	private Integer requestTypeId;
	
	private String requestType;

	private List<EnqSendToVendorDetailRespDto> items;

	public QuotationDetailsResponse() {
		super();
	}
	
	public QuotationDetailsResponse(Integer id,String qrNumber,Date qrDate,Integer assetTypeId,String assetType,Integer requestTypeId,String requestType) {
		super();
		this.id = id;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.requestTypeId = requestTypeId;
		this.requestType = requestType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}
	
	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public List<EnqSendToVendorDetailRespDto> getItems() {
		return items;
	}

	public void setItems(List<EnqSendToVendorDetailRespDto> items) {
		this.items = items;
	}
	
	public void addItems(EnqSendToVendorDetailRespDto item) {
		if (this.items == null)
			this.items = new ArrayList<EnqSendToVendorDetailRespDto>();
		
		this.items.add(item);
	}
	
	
}
