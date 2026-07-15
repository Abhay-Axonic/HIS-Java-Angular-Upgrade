package com.param.procurement.workOrder.dto.workOrderCreation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WOCreationBatchResponse {
	
	private Integer batchId;
	
	private String batchNo;
	
	private String batchDetails;
	
	private Date expiry;
	
	private Integer currentVendorId;
	
	private String currentVendorName;

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBatchDetails() {
		return batchDetails;
	}

	public void setBatchDetails(String batchDetails) {
		this.batchDetails = batchDetails;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Integer getCurrentVendorId() {
		return currentVendorId;
	}

	public void setCurrentVendorId(Integer currentVendorId) {
		this.currentVendorId = currentVendorId;
	}

	public String getCurrentVendorName() {
		return currentVendorName;
	}

	public void setCurrentVendorName(String currentVendorName) {
		this.currentVendorName = currentVendorName;
	}
	
	public WOCreationBatchResponse()
	{
		super();
	}
}
