package com.param.procurement.quotation.dto.quotationRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuotationRequestResponse {

	private Integer id;

	private String qrNumber;
	
	private Date qrDate;

	private Integer storeId;

	private String storeName;

	private Integer assetTypeId;

	private String assetType;

	private Integer requestTypeId;

	private String requestType;

	private String remark;

	private Integer statusId;

	private String status;

	private Integer approvalStatusId;

	private String approvalStatus;
	
	private String requestedBy;
	
	private List<QuotationRequestDetailsResponse> items;

	public QuotationRequestResponse() {
		super();
		
	}

	public QuotationRequestResponse(Integer id, String qrNumber,Date qrDate, Integer storeId, String storeName, Integer assetTypeId,
			String assetType, Integer requestTypeId, String requestType, String remark, Integer statusId, String status,
			Integer approvalStatusId, String approvalStatus, String requestedBy) {
		super();
		this.id = id;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.storeId = storeId;
		this.storeName = storeName;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.requestTypeId = requestTypeId;
		this.requestType = requestType;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
		this.requestedBy = requestedBy;
		
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<QuotationRequestDetailsResponse> getItems() {
		return items;
	}

	public void setItems(List<QuotationRequestDetailsResponse> items) {
		this.items = items;
	}
	
	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}
	
	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public void addItems(QuotationRequestDetailsResponse item) {
		if (this.items == null)
			this.items = new ArrayList<QuotationRequestDetailsResponse>();
		
		this.items.add(item);
	}

}
