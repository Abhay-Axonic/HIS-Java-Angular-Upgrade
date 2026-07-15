package com.param.procurement.purchase.purchaseOrder.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseOrderSearchDto {
	    	
    private Integer id;
    
    private Date purchaseDate;

    private String purchaseNo;
    
    private Integer storeId;
    
	private String storeName;
	
	private Integer requestingStoreId;
	    
	private String requestingStoreName;
	    
    
    private String type;
    
    private String purchaseType;
    
    private String approvalStatus;
    
    private String status;
    
    private String addedBy;
    
    private String remark;
    
    private String vendorName;  
    
    private Date addedDate;
    
    private Double poAmount;
    
    private Double grnAmount;
    
    /* added by minesh for consignment (true/false) */
    private Boolean isConsignment;
    
    private Integer reviewedBy;
        
    public Integer getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(Integer reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}
	
	public PurchaseOrderSearchDto(Integer id, Date purchaseDate, String purchaseNo, String storeName, Integer storeId,String type,
			String purchaseType, String approvalStatus, String status, String addedBy, String remark,
			String vendorName,Date addedDate, Integer requestingStoreId ,String  requestingStoreName, Boolean isConsignment) {
		super();
		this.id = id;
		this.purchaseDate = purchaseDate;
		this.purchaseNo = purchaseNo;
		this.storeName = storeName;
		this.storeId = storeId;
		this.type = type;
		this.purchaseType = purchaseType;
		this.approvalStatus = approvalStatus;
		this.status = status;
		this.addedBy = addedBy;
		this.remark = remark;
		this.vendorName = vendorName;
		this.addedDate = addedDate;
		this.requestingStoreId = requestingStoreId;
		this.requestingStoreName = requestingStoreName;
		this.isConsignment = isConsignment;
	}   
	/* end added by minesh for consignment (true/false) */

	public PurchaseOrderSearchDto() {
		super();
	}
    
    public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
    
    public PurchaseOrderSearchDto(Integer id, Date purchaseDate, String purchaseNo, String storeName, Integer storeId,String type,
			String purchaseType, String approvalStatus, String status, String addedBy, String remark,
			String vendorName,Date addedDate, Integer requestingStoreId ,String  requestingStoreName) {
		super();
		this.id = id;
		this.purchaseDate = purchaseDate;
		this.purchaseNo = purchaseNo;
		this.storeName = storeName;
		this.storeId = storeId;
		this.type = type;
		this.purchaseType = purchaseType;
		this.approvalStatus = approvalStatus;
		this.status = status;
		this.addedBy = addedBy;
		this.remark = remark;
		this.vendorName = vendorName;
		this.addedDate = addedDate;
		this.requestingStoreId = requestingStoreId;
		this.requestingStoreName = requestingStoreName;
	}   

   	public PurchaseOrderSearchDto(Integer id, Date purchaseDate, String purchaseNo, String storeName, 
			 String approvalStatus, String status, String addedBy, String vendorName,
			 Double poAmount, Double grnAmount, String requestingStoreName) {
		this.id = id;
		this.purchaseDate = purchaseDate;
		this.purchaseNo = purchaseNo;
		this.storeName = storeName;
		this.approvalStatus = approvalStatus;
		this.status = status;
		this.addedBy = addedBy;
		this.vendorName = vendorName;
		this.poAmount = poAmount;
		this.grnAmount = grnAmount;
		this.requestingStoreName = requestingStoreName;
	}

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	public String getRequestingStoreName() {
		return requestingStoreName;
	}

	public void setRequestingStoreName(String requestingStoreName) {
		this.requestingStoreName = requestingStoreName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPurchaseNo() {
		return purchaseNo;
	}

	public void setPurchaseNo(String purchaseNo) {
		this.purchaseNo = purchaseNo;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Double getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(Double poAmount) {
		this.poAmount = poAmount;
	}

	public Double getGrnAmount() {
		return grnAmount;
	}

	public void setGrnAmount(Double grnAmount) {
		this.grnAmount = grnAmount;
	}

}
