package com.param.procurement.quotation.dto.quotationFill;

import java.util.Date;
import java.util.List;

public class QuotationFillSearchResponse {

	private Integer id;

	private String qrNumber;

	private Date qrDate;

	private String vqrNumber;

	private Date vqrDate;

	private String storeName;

	private String vendorName;

	private String requestedBy;

	private String status;

	private String approvalStatus;
	
	private VendorDetails vendorDetails;

	public QuotationFillSearchResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuotationFillSearchResponse(Integer id, String qrNumber, Date qrDate, String vqrNumber, Date vqrDate,
			String storeName, String vendorName, String requestedBy, String status, String approvalStatus,VendorDetails vendorDetails) {
		super();
		this.id = id;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.vqrNumber = vqrNumber;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.vendorName = vendorName;
		this.requestedBy = requestedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.vendorDetails = vendorDetails;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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

	public VendorDetails getVendorDetails() {
		return vendorDetails;
	}

	public void setVendorDetails(VendorDetails vendorDetails) {
		this.vendorDetails = vendorDetails;
	}
	
}
