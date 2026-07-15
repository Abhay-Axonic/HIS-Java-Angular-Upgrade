package com.param.procurement.quotation.dto.enqSendToVendor;

import java.util.Date;

public class EnqSendToVendorSearchDto {

	private Integer id;

	private String qrNumber;

	private Date qrDate;

	private String vqrNumber;

	private Date vqrDate;

	private String storeName;

	private String vendorName;

	private Date dueDate;

	private String requestedBy;

	private String status;

	private String approvalStatus;

	public EnqSendToVendorSearchDto(Integer id, String qrNumber, Date qrDate, String vqrNumber, Date vqrDate,
			String storeName, String vendorName, Date dueDate, String requestedBy, String status,
			String approvalStatus) {
		super();
		this.id = id;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.vqrNumber = vqrNumber;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.vendorName = vendorName;
		this.dueDate = dueDate;
		this.requestedBy = requestedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
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

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
	

}
