package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;

public class GRNSearchResponse {
	
	private Integer id;
	
	private Integer storeId;
	  
    private String storeName;
    
    private Integer requestingStoreId;
	  
    private String requestingStoreName;
    
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

	private String grnNo;
    
    private Date grnDate;
    
    private String invoiceNo;
    
    private Date invoiceDate;
    
    private String dcNo;
    
    private Date dcDate;
    
    private String vendorName;
    
    private String addedBy;
    
    private String status;
    
    private String approvalStatus;
    
    private String approvedBy;

   	private Date approvedDate;
    
   	
   	
    public GRNSearchResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GRNSearchResponse(Integer id,Integer storeId,String storeName, String grnNo, Date grnDate, String invoiceNo, Date invoiceDate, String dcNo,
			Date dcDate, String vendorName, String addedBy, String status, String approvalStatus) {
		super();
		this.id = id;
		this.storeId=storeId;
		this.storeName=storeName;
		this.grnNo = grnNo;
		this.grnDate = grnDate;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
		this.vendorName = vendorName;
		this.addedBy = addedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
	}
    
    public GRNSearchResponse(Integer id,Integer storeId,String storeName, String grnNo, Date grnDate, String invoiceNo, Date invoiceDate, String dcNo,
			Date dcDate, String vendorName, String addedBy, String status, String approvalStatus, String approvedBy,
			Date approvedDate) {
		super();
		this.id = id;
		this.storeId=storeId;
		this.storeName=storeName;
		this.grnNo = grnNo;
		this.grnDate = grnDate;
		this.invoiceNo = invoiceNo;
		this.invoiceDate = invoiceDate;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
		this.vendorName = vendorName;
		this.addedBy = addedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDcNo() {
		return dcNo;
	}

	public void setDcNo(String dcNo) {
		this.dcNo = dcNo;
	}

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
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

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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
//	Added By Atmiya 20191108

	private Boolean IsConsignment;

	public Boolean getIsConsignment() {
		return IsConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.IsConsignment = isConsignment;
	}
// END Added By Atmiya 20191108
	
}
