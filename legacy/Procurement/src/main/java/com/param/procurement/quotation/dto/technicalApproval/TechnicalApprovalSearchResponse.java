package com.param.procurement.quotation.dto.technicalApproval;

import java.util.Date;

public class TechnicalApprovalSearchResponse {

	private Integer id;

	private Date qrDate;

	private String qrNumber;

	private String storeName;

	private String qrAgainst;
	
	private String assetType;

	private String requestedBy;

	private Date requestedOn;

	private String status;

	
	
	public TechnicalApprovalSearchResponse(Integer id, Date qrDate, String qrNumber, String storeName,String qrAgainst,String assetType,
			String requestedBy, Date requestedOn, String status) {
		super();
		this.id = id;
		this.qrDate = qrDate;
		this.qrNumber = qrNumber;
		this.storeName = storeName;
		this.qrAgainst = qrAgainst;
		this.assetType = assetType;
		this.requestedBy = requestedBy;
		this.requestedOn = requestedOn;
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

	public Date getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(Date requestedOn) {
		this.requestedOn = requestedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	
}
