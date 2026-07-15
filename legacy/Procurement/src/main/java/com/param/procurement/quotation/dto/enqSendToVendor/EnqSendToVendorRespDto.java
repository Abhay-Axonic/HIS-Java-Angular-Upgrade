package com.param.procurement.quotation.dto.enqSendToVendor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDetailsResponse;

public class EnqSendToVendorRespDto {
	
	private Integer id;
	
	private Integer qrId;

    private String qrNumber;
    
    private Date qrDate;
    
    private String vqrNumber;
    
    private Date vqrDate;
    
    private String storeName;
    
    private Integer storeId;
    
    private Integer assetTypeId;
    
    private String assetType;
    
    private Integer requestTypeId;
    
    private String requestType;
    
    private String vendorName;
    
    private Integer vendorId;
    
    private Date dueDate;
    
    private String requestedBy;
    
    private String status;
    
    private Integer statusId;
    
    private String approvalStatus;
    
    private Integer approvalStatusId;
    
    private List<EnqSendToVendorDetailRespDto> items;
    
    
    public EnqSendToVendorRespDto(Integer id, Integer qrId,String qrNumber, Date qrDate, String vqrNumber, Date vqrDate,
			String storeName, Integer storeId, Integer assetTypeId,String assetType,Integer requestTypeId,String requestType,String vendorName, Integer vendorId, Date dueDate, String requestedBy,
			String status, Integer statusId, String approvalStatus, Integer approvalStatusId) {
		super();
		this.id = id;
		this.qrId = qrId;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.vqrNumber = vqrNumber;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.storeId = storeId;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.requestTypeId = requestTypeId;
		this.requestType = requestType;
		this.vendorName = vendorName;
		this.vendorId = vendorId;
		this.dueDate = dueDate;
		this.requestedBy = requestedBy;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
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

	public String getVqrNumber() {
		return vqrNumber;
	}

	public void setVqrNumber(String vqrNumber) {
		this.vqrNumber = vqrNumber;
	}

	public Date getVqrDate() {
		return vqrDate;
	}

	public void setVqrDate(Date vqrDate) {
		this.vqrDate = vqrDate;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
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

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
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
