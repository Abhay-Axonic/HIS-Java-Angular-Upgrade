package com.param.procurement.quotation.dto.quotationRequest;

import java.util.Date;

public class QuotationRequestSearchResponse {
	
	private Integer id;

	private String  qrNumber;
	
	private Date qrDate;
	
	private String storeName;
	
	private String assetType;
	
	private String requestType;
	
	private String remark;
	
	private String requestedBy;
	
	private String status;
	
	private String approvalStatus;
	
	private Boolean technicalApproval;
	
	private Boolean vendorSelection;
	
	private Boolean readyForTechnicalApproval;
	
	public QuotationRequestSearchResponse() {
		super();
	}
	
	public QuotationRequestSearchResponse(Integer id, String qrNumber, Date qrDate, String storeName, String assetType,
			String requestType, String remark, String requestedBy, String status, String approvalStatus,
			Boolean technicalApproval, Boolean vendorSelection, Boolean readyForTechnicalApproval) {
		super();
		this.id = id;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.storeName = storeName;
		this.assetType = assetType;
		this.requestType = requestType;
		this.remark = remark;
		this.requestedBy = requestedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.technicalApproval = technicalApproval;
		this.vendorSelection = vendorSelection;
		this.readyForTechnicalApproval = readyForTechnicalApproval;
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

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Boolean getTechnicalApproval() {
		return technicalApproval;
	}

	public void setTechnicalApproval(Boolean technicalApproval) {
		this.technicalApproval = technicalApproval;
	}

	public Boolean getVendorSelection() {
		return vendorSelection;
	}

	public void setVendorSelection(Boolean vendorSelection) {
		this.vendorSelection = vendorSelection;
	}

	public Boolean getReadyForTechnicalApproval() {
		return readyForTechnicalApproval;
	}

	public void setReadyForTechnicalApproval(Boolean readyForTechnicalApproval) {
		this.readyForTechnicalApproval = readyForTechnicalApproval;
	}
	
}
