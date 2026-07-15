package com.param.procurement.quotation.dto.vendorSelection;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class VendorSelectionRequest extends CookieObjectDto{
	
	private Integer id;

	private Date fromDate;

	private Date toDate;

	private Integer storeId;

	private Integer statusId;
	
	private Integer cancelReasonId;
	
	private String cancelNote;
	
	private List<VendorSelectionDetailRequest> items;

	public VendorSelectionRequest() {
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

	public List<VendorSelectionDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<VendorSelectionDetailRequest> items) {
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
	
}
