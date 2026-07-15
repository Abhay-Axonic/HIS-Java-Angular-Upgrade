package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;

import javax.persistence.Column;

public class GRNSearchResponse1 {
	
	private Integer id;
	
	private Integer storeId;
	  
    private String storeName;
    
    private Integer requestingStoreId;
    private Integer vendorId;
	  
    public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	private String requestingStoreName;
    private String billType;
    
    public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
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
	private Double netAmount;
	private Double paidAmount;
	private Double balanceAmount;
	private String grnNo;
	private String paymentStatus;
	private Double roundOffAmount;
    
    public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	private Date grnDate;
    
    private String invoiceNo;
    
    private Date invoiceDate;
    
    public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	private String dcNo;
    
    private Date dcDate;
    
    private String vendorName;
    
    private String addedBy;
    
    private String status;
    
    private String approvalStatus;
    
    private String approvedBy;

   	private Date approvedDate;
   	
   	private Double totalDiscount;
   	
   	private Double totalCreditAmount;
    private Date payDueDate;


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

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalCreditAmount() {
		return totalCreditAmount;
	}

	public void setTotalCreditAmount(Double totalCreditAmount) {
		this.totalCreditAmount = totalCreditAmount;
	}

	public Double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
	}

	public Date getPayDueDate() {
		return payDueDate;
	}

	public void setPayDueDate(Date payDueDate) {
		this.payDueDate = payDueDate;
	}
    
	
}
