package com.param.procurement.quotation.dto.vendorNegotiation;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class VendorNegotiationRequest extends CookieObjectDto {
	
	private Integer id;

	private Date fromDate;

	private Date toDate;

	private Integer storeId;

    private Integer statusId;
    
    private Integer approvalStatusId;

	private Integer vendorId;

	private String vendorName;
	
	private Integer cancelReasonId;
	
	private String cancelNote;
	
	private Integer qrId;
	
	private List<VendorNegotiationDetailsRequest> items;

	public VendorNegotiationRequest() {
		super();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<VendorNegotiationDetailsRequest> getItems() {
		return items;
	}

	public void setItems(List<VendorNegotiationDetailsRequest> items) {
		this.items = items;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}
	
}
